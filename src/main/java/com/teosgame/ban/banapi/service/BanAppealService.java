package com.teosgame.ban.banapi.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.amazonaws.HttpMethod;
import com.teosgame.ban.banapi.exception.BadRequestException;
import com.teosgame.ban.banapi.exception.ForbiddenException;
import com.teosgame.ban.banapi.exception.NotFoundException;
import com.teosgame.ban.banapi.model.entity.AppealEntity;
import com.teosgame.ban.banapi.model.entity.JudgementEntity;
import com.teosgame.ban.banapi.model.enums.BanType;
import com.teosgame.ban.banapi.model.enums.JudgementStatus;
import com.teosgame.ban.banapi.model.request.CreateBanAppealRequest;
import com.teosgame.ban.banapi.model.request.UpdateBanAppealRequest;
import com.teosgame.ban.banapi.model.response.BanAppealResponse;
import com.teosgame.ban.banapi.model.response.BanAppealsResponse;
import com.teosgame.ban.banapi.model.response.EvidenceResponse;
import com.teosgame.ban.banapi.model.response.JudgementResponse;
import com.teosgame.ban.banapi.persistence.BanAppealRepository;
import com.teosgame.ban.banapi.util.BanUtils;
import com.teosgame.ban.banapi.validators.BanAppealValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BanAppealService {

    private final BanUtils utils;
    private final BanAppealRepository repository;
    private final S3Service s3Service;
    private final BanAppealValidator validator;

    Logger logger = LoggerFactory.getLogger(BanAppealService.class);

    /**
     * When displaying in  a list form it's probably best to just display the number and the status. 
     * ID returned so we can grab the individual object upon potential click
     */
    public BanAppealsResponse getBanAppeals(String username, 
        String banType, String judgementStatus, int pageCount, int pageSize) 
        throws BadRequestException {
        String twitchUsername = SecurityContextHolder.getContext()
            .getAuthentication().getName();

        logger.info("User {} getting ban appeals with parameters: name - {} status - {} type - {}.", twitchUsername, username, judgementStatus, banType);
        List<AppealEntity> entities;
        long totalPages;
        long totalSize;

        if (!utils.isUserAdmin()) {
            // Since users probably won't have 100 appeals, we won't let them filter it out at all
            entities = repository.findByTwitchUsername(twitchUsername);
            totalSize = entities.size();
            totalPages = totalSize / pageSize;
        } else { // only admins can see evidence
            Page<AppealEntity> page = getAppealsByFilters(username, banType, judgementStatus, pageCount, pageSize);
            entities = page.getContent();
            totalSize = page.getTotalElements();
            totalPages = page.getTotalPages();
        }

        return new BanAppealsResponse(pageCount, pageSize, totalPages, totalSize,
            entities.stream().map(entity -> {
              return BanAppealResponse.builder()
                  .appealId(entity.getId())
                  .banType(entity.getBanType())
                  .judgement(new JudgementResponse(entity.getJudgement()))
              .build();
          }).collect(Collectors.toList())
        );

    }

    public BanAppealResponse getBanAppeal(String appealId) throws NotFoundException, ForbiddenException {
        String twitchUsername = SecurityContextHolder.getContext()
            .getAuthentication().getName();
        
        logger.info("User {} getting ban appeal by ID {}.", twitchUsername, appealId);

        AppealEntity entity = repository.findById(appealId).orElse(null);

        validator.validateGetRequest(entity, appealId, twitchUsername);

        List<EvidenceResponse> evidence = null;
        String prevPageId = null;
        String nextPageId = null;

        // Admins can view all and submitters can view their own appeals
        if (utils.isUserAdmin() && entity.getEvidence() != null) { // only admins can see evidence
            evidence = entity.getEvidence().stream().map(evidenceEntity -> {
                String filePath = entity.getId() + "/evidence/" + evidenceEntity.getId() + evidenceEntity.getFileExtension();
                return new EvidenceResponse(evidenceEntity, evidenceEntity.getBannedBy(), s3Service.generatePreSignedUrl(filePath, HttpMethod.GET).toString());
            }).collect(Collectors.toList());

            prevPageId = repository.findAppealIdBefore(entity.getCreatedAt()).orElse(null);
            nextPageId = repository.findAppealIdAfter(entity.getCreatedAt()).orElse(null);
        }

        String previousId = entity.getPrevious() != null ? entity.getPrevious().getId() : null;

        return BanAppealResponse.fromEntity(entity, previousId, evidence, prevPageId, nextPageId);
    }
    
    public String createBanAppeal(CreateBanAppealRequest request) 
        throws BadRequestException, NotFoundException {
        String twitchUsername = SecurityContextHolder.getContext()
            .getAuthentication().getName();

        logger.info("User {} creating ban appeal, {}", twitchUsername, request.getTwitchUsername());

        // Ignore add ons unless they are resubmitting
        String additionalData = null;
        AppealEntity previous = repository.findById(request.getPreviousAppealId()).orElse(null);

        validator.validateCreateRequest(
            request, 
            twitchUsername,
            repository.countPendingByUsername(request.getTwitchUsername()),
            previous);

        if (previous != null) {
            additionalData = request.getAdditionalData();
        }

        AppealEntity entity = AppealEntity.fromRequest(request, previous, additionalData);

        // Setting values that have to be made post builder
        entity.setJudgement(JudgementEntity.builder()
            .appeal(entity)
            .status(JudgementStatus.PENDING)
            .createdBy("default")
            .build());

        entity = repository.save(entity);
        
        return entity.getId();
    }

    public void updateBanAppeal(String appealId, UpdateBanAppealRequest request) 
        throws BadRequestException, NotFoundException, ForbiddenException {
        String twitchUsername = SecurityContextHolder.getContext()
            .getAuthentication().getName();

        logger.info("User {} updating ban appeal with ID {}.", twitchUsername, appealId);
        
        AppealEntity entity = repository.findById(appealId).orElse(null);

        validator.validateUpdateRequest(request, entity, appealId, twitchUsername);

        entity.updateEntityFromRequest(request, twitchUsername);

        entity = repository.save(entity);;
    }

    public void deleteBanAppeal(String appealId)
        throws BadRequestException, ForbiddenException, NotFoundException {

        String twitchUsername = SecurityContextHolder.getContext()
            .getAuthentication().getName();

        logger.info("User {} deleting ban appeal with ID {}.", twitchUsername, appealId);
        
        AppealEntity entity = repository.findById(appealId).orElse(null);

        validator.validateDeleteRequest(entity, appealId, twitchUsername);

        repository.delete(entity);
    }

    private Page<AppealEntity> getAppealsByFilters(String username, String banType, String judgementStatus, int pageCount, int pageSize)
        throws BadRequestException {
        if (banType != null) {
            BanType.fromType(banType);
            banType = banType.toUpperCase();
        }

        if (judgementStatus != null) {
            JudgementStatus.fromStatus(judgementStatus);
            judgementStatus = judgementStatus.toUpperCase().replace(" ", "_"); 
        }

        return repository.findByUsernameAndBanTypeAndJudgmentStatus(
            username,
            banType, 
            judgementStatus, 
            PageRequest.of(pageCount - 1, pageSize));
    }
}
