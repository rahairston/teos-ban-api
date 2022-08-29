package com.teosgame.ban.banapi.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

    Logger logger = LoggerFactory.getLogger(EvidenceService.class);

    public EvidenceResponse getEvidence(String appealId) throws NotFoundException, ForbiddenException {
        if (!utils.isUserAdmin()) {
            logger.error("User {} attempted to modify banned by data", SecurityContextHolder.getContext()
                    .getAuthentication().getName());
            throw new ForbiddenException("Only admins can get Banned By data");
        }

        AppealEntity entity = repository.findById(appealId).orElse(null);

        return null;
    }

    public String createEvidence(String appealId, EvidenceRequest request)
            throws ForbiddenException, NotFoundException {

        AppealEntity entity = preEvidenceCheck(appealId);

        return "";
    }

    public void updateEvidence(String appealId, String evidenceId, EvidenceRequest request)
            throws BadRequestException, NotFoundException, ForbiddenException {
        AppealEntity entity = preEvidenceCheck(appealId);

        entity = repository.save(entity);
    }

    public void deleteEvidence(String appealId, String evidenceId)
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
    }

    private AppealEntity preEvidenceCheck(String appealId) throws ForbiddenException, NotFoundException {
        if (!utils.isUserAdmin()) {
          logger.error("User {} attempted to modify banned by data", SecurityContextHolder.getContext()
                  .getAuthentication().getName());
            throw new ForbiddenException("Only admins can get Banned By data");
        }

        AppealEntity entity = repository.findById(appealId).orElse(null);

        if (entity == null) {
            throw new NotFoundException("Appeal with ID " + appealId + " not found");
        }

        return entity;
    }

}
