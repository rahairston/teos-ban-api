package com.teosgame.ban.banapi.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.teosgame.ban.banapi.exception.BadRequestException;
import com.teosgame.ban.banapi.exception.ForbiddenException;
import com.teosgame.ban.banapi.exception.NotFoundException;
import com.teosgame.ban.banapi.model.entity.AppealEntity;
import com.teosgame.ban.banapi.model.entity.JudgementEntity;
import com.teosgame.ban.banapi.model.enums.JudgementStatus;
import com.teosgame.ban.banapi.model.request.JudgementRequest;
import com.teosgame.ban.banapi.persistence.BanAppealRepository;
import com.teosgame.ban.banapi.util.BanUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JudgementService {

    private final BanUtils utils;
    private final BanAppealRepository repository;

    Logger logger = LoggerFactory.getLogger(JudgementService.class);
    
    public void updateJudgement(String appealId, JudgementRequest request) throws ForbiddenException, NotFoundException, BadRequestException {
        String username = SecurityContextHolder.getContext()
            .getAuthentication().getName();
        if (!utils.isUserAdmin()) {
            logger.error("User {} attempted to modify judgement status.", username);
            throw new ForbiddenException("Only admins can update judgement status.");
        }

        AppealEntity entity = repository.findById(appealId).orElse(null);

        if (entity == null) {
            throw new NotFoundException("Appeal with ID " + appealId + " not found");
        }

        JudgementEntity judgement = entity.getJudgement();
        judgement.setStatus(JudgementStatus.fromStatus(request.getStatus()));
        judgement.setNotes(request.getNotes());
        judgement.setResubmitAfterDate(request.getResubmitAfterDate());

        Date now = new Date();

        judgement.setModifiedAt(now);
        judgement.setModifiedBy(username);
        entity.setModifiedAt(now);
        entity.setModifiedBy(username);

        repository.save(entity);
    }
}
