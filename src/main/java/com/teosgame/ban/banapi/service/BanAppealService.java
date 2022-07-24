package com.teosgame.ban.banapi.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.amazonaws.HttpMethod;
import com.teosgame.ban.banapi.exception.BadRequestException;
import com.teosgame.ban.banapi.exception.ForbiddenException;
import com.teosgame.ban.banapi.exception.NotFoundException;
import com.teosgame.ban.banapi.model.entity.AppealEntity;
import com.teosgame.ban.banapi.model.entity.JudgementEntity;
import com.teosgame.ban.banapi.model.enums.JudgementStatus;
import com.teosgame.ban.banapi.model.request.CreateBanAppealRequest;
import com.teosgame.ban.banapi.model.request.UpdateBanAppealRequest;
import com.teosgame.ban.banapi.model.response.BanAppealResponse;
import com.teosgame.ban.banapi.model.response.EvidenceResponse;
import com.teosgame.ban.banapi.model.response.JudgementResponse;
import com.teosgame.ban.banapi.persistence.BanAppealRepository;
import com.teosgame.ban.banapi.util.BanUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BanAppealService {

    private final BanUtils utils;
    private final BanAppealRepository repository;
    private final S3Service s3Service;

    Logger logger = LoggerFactory.getLogger(BanAppealService.class);

    public BanAppealResponse getBanAppeal(String appealId) throws NotFoundException, ForbiddenException {
        String twitchUserName = SecurityContextHolder.getContext()
            .getAuthentication().getName();
        
        logger.info("User {} getting ban appeal by ID {}.", twitchUserName, appealId);

        AppealEntity entity = repository.findById(appealId).orElse(null);

        if (entity == null) {
            throw new NotFoundException("Appeal with id " + appealId + " does not exist");
        }

        List<EvidenceResponse> evidence = null;

        // Admins can view all and submitters can view their own appeals
        if (!utils.isUserAdmin()) {
            if (!entity.getTwitchUsername().equalsIgnoreCase(twitchUserName)) {
                throw new ForbiddenException("User does not own this appeal");
            }
        } else if (entity.getEvidence() != null) { // only admins can see evidence
            evidence = entity.getEvidence().stream().map(evidenceEntity -> {
                String filePath = entity.getId() + "/evidence/" + evidenceEntity.getId() + evidenceEntity.getFileExtension();
                return new EvidenceResponse(evidenceEntity, s3Service.generatePreSignedUrl(filePath, HttpMethod.GET).toString());
            }).collect(Collectors.toList());
        }

        String previousId = entity.getPrevious() != null ? entity.getPrevious().getId() : null;

        return BanAppealResponse.builder()
            .appealId(entity.getId())
            .twitchUsername(entity.getTwitchUsername())
            .discordUsername(entity.getDiscordUsername())
            .banType(entity.getBanType())
            .banReason(entity.getBanReason())
            .banJustified(entity.getBanJustified())
            .appealReason(entity.getAppealReason())
            .additionalNotes(entity.getAdditionalNotes())
            .previousAppealId(previousId)
            .additionalData(entity.getAdditionalData())
            .evidence(evidence)
            .judgement(new JudgementResponse(entity.getJudgement()))
        .build();
    }
    
    public String createBanAppeal(CreateBanAppealRequest request) 
        throws BadRequestException, NotFoundException {
        String twitchUserName = SecurityContextHolder.getContext()
            .getAuthentication().getName();

        logger.info("User {} creating ban appeal.", twitchUserName);
        
        if (!request.getTwitchUsername().equalsIgnoreCase(twitchUserName)) {
            throw new BadRequestException("User submitting ban does not match user name in appeal");
        }

        // Ignore add ons unless they are resubmitting
        String additionalData = null;
        AppealEntity previous = null;

        if (request.getPreviousAppealId() != null) {
            additionalData = request.getAdditionalData();
            previous = repository.findById(request.getPreviousAppealId()).orElse(null);
            if (previous == null) {
                throw new NotFoundException("Previous Ban Appeal with id " + request.getPreviousAppealId() + " not found");
            }

            if (!previous.getJudgement().getStatus().isResubmissionRequired()) {
                throw new BadRequestException("Previous Ban Appeal does not require a resubmission");
            }
        }

        AppealEntity entity = AppealEntity.builder()
            .twitchUsername(request.getTwitchUsername())
            .discordUsername(request.getDiscordUsername())
            .banType(request.getBanType())
            .banReason(request.getBanReason())
            .banJustified(request.getBanJustified())
            .appealReason(request.getAppealReason())
            .additionalNotes(request.getAdditionalNotes())
            .additionalData(additionalData)
            .previous(previous)
            .createdBy(request.getTwitchUsername())
            .build();

        // Setting values that have to be made post builder
        entity.setJudgement(JudgementEntity.builder()
            .appeal(entity)
            .status(JudgementStatus.PENDING)
            .createdBy("default")
            .build());

        entity = repository.save(entity);
        
        return entity.getId();
    }

    public BanAppealResponse updateBanAppeal(String appealId, UpdateBanAppealRequest request) 
        throws BadRequestException, NotFoundException {
        String twitchUserName = SecurityContextHolder.getContext()
            .getAuthentication().getName();

        logger.info("User {} updating ban appeal with ID {}.", twitchUserName, appealId);
        
        AppealEntity entity = repository.findById(appealId).orElse(null);

        if (entity == null) {
            throw new NotFoundException("Appela with id " + appealId + " does not exist");
        }

        if (!utils.isUserAdmin()) {
            if (!request.getTwitchUsername().equalsIgnoreCase(twitchUserName)) {
                throw new BadRequestException("User submitting ban does not match user name in appeal");
            }

            if (!entity.getJudgement().getStatus().isPending()) {
                throw new BadRequestException("User cannot edit Appeal once evidence has been submitted.");
            }
        } else {

        }

        return BanAppealResponse.builder()
            .build();
    }
}
