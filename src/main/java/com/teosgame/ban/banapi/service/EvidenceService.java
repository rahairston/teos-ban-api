package com.teosgame.ban.banapi.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.amazonaws.HttpMethod;
import com.teosgame.ban.banapi.exception.BadRequestException;
import com.teosgame.ban.banapi.exception.ForbiddenException;
import com.teosgame.ban.banapi.exception.NotFoundException;
import com.teosgame.ban.banapi.model.entity.AppealEntity;
import com.teosgame.ban.banapi.model.entity.EvidenceEntity;
import com.teosgame.ban.banapi.model.request.EvidenceRequest;
import com.teosgame.ban.banapi.model.response.EvidenceResponse;
import com.teosgame.ban.banapi.persistence.BanAppealRepository;
import com.teosgame.ban.banapi.util.BanUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EvidenceService {

    private final BanUtils utils;
    private final BanAppealRepository repository;
    private final S3Service service;

    Logger logger = LoggerFactory.getLogger(EvidenceService.class);

    public EvidenceResponse getEvidence(String appealId, String evidenceId) throws NotFoundException, ForbiddenException {
        AppealEntity entity = preEvidenceCheck(appealId);

        EvidenceEntity evidence = entity.getEvidence().stream().filter(e -> {
          return e.getId().equalsIgnoreCase(evidenceId);
        }).findFirst().orElse(null);

        if (evidence == null) {
            throw new NotFoundException("Evidence with ID " + evidenceId + " not found.");
        }

        String filePath = entity.getTwitchUsername() + "/" + entity.getId() + "/" + evidenceId + "." + evidence.getFileExtension();

        return new EvidenceResponse(evidence, service.generatePreSignedUrl(filePath, HttpMethod.GET).toString());
    }

    public EvidenceResponse createEvidence(String appealId, EvidenceRequest request)
            throws ForbiddenException, NotFoundException {

        AppealEntity entity = preEvidenceCheck(appealId);

        EvidenceEntity evidence = EvidenceEntity.builder()
            .appeal(entity)
            .notes(request.getNotes())
            .fileExtension(getFileExtension(request.getFileName()))
            .createdBy(SecurityContextHolder.getContext().getAuthentication().getName())
            .build();

        entity.addEvidence(evidence);

        repository.save(entity);

        String filePath = entity.getTwitchUsername() + "/" + entity.getId() + "/" + evidence.getId();

        return new EvidenceResponse(evidence, service.generatePreSignedUrl(filePath, HttpMethod.PUT).toString());
    }

    public EvidenceResponse updateEvidence(String appealId, String evidenceId, EvidenceRequest request)
            throws BadRequestException, NotFoundException, ForbiddenException {
        AppealEntity entity = preEvidenceCheck(appealId);

        EvidenceEntity evidence = entity.getEvidence().stream().filter(e -> {
          return e.getId().equalsIgnoreCase(evidenceId);
        }).findFirst().orElse(null);

        if (evidence == null) {
            throw new NotFoundException("Evidence with ID " + evidenceId + " not found");
        }

        if (request.getFileName() != null) {
            evidence.setFileExtension(getFileExtension(request.getFileName()));
        }

        if (request.getNotes() != null) {
            evidence.setNotes(request.getNotes());
        }

        evidence.setModifiedAt(new Date());
        entity.setModifiedAt(new Date());
        evidence.setModifiedBy(SecurityContextHolder.getContext()
        .getAuthentication().getName());
        entity.setModifiedBy(SecurityContextHolder.getContext()
        .getAuthentication().getName());

        repository.save(entity);

        String filePath = entity.getTwitchUsername() + "/" + entity.getId() + "/" + evidence.getId() + "." + evidence.getFileExtension();

        return new EvidenceResponse(evidence, service.generatePreSignedUrl(filePath, HttpMethod.PUT).toString());
    }

    public EvidenceResponse deleteEvidence(String appealId, String evidenceId)
            throws ForbiddenException, NotFoundException {

        AppealEntity entity = preEvidenceCheck(appealId);

        EvidenceEntity deleted = entity.getEvidence().stream().filter(evidence -> {
          return evidence.getId().equalsIgnoreCase(evidenceId);
        }).findFirst().orElse(null);

        if (deleted == null) {
            throw new NotFoundException("Evidence with ID " + evidenceId + " not found");
        }

        entity.removeEvidence(deleted);

        repository.save(entity);

        String filePath = entity.getTwitchUsername() + "/" + entity.getId() + "/" + deleted.getId() + "." + deleted.getFileExtension();

        return new EvidenceResponse(deleted, service.generatePreSignedUrl(filePath, HttpMethod.DELETE).toString());
    }

    private AppealEntity preEvidenceCheck(String appealId) throws ForbiddenException, NotFoundException {
        if (!utils.isUserAdmin()) {
          logger.error("User {} attempted to modify or get evidence data", SecurityContextHolder.getContext()
                  .getAuthentication().getName());
            throw new ForbiddenException("Only admins can modify or get evidence data.");
        }

        AppealEntity entity = repository.findById(appealId).orElse(null);

        if (entity == null) {
            throw new NotFoundException("Appeal with ID " + appealId + " not found");
        }

        return entity;
    }

    private String getFileExtension(String fileName) {
        String extension = "";

        int i = fileName.lastIndexOf('.');
        int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

        if (i > p) {
            extension = fileName.substring(i+1);
        }

        return extension;
    }

}
