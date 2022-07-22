package com.teosgame.ban.banapi.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.teosgame.ban.banapi.exception.BadRequestException;
import com.teosgame.ban.banapi.exception.NotFoundException;
import com.teosgame.ban.banapi.model.entity.AppealEntity;
import com.teosgame.ban.banapi.model.enums.BanStatus;
import com.teosgame.ban.banapi.model.request.CreateBanAppealRequest;
import com.teosgame.ban.banapi.model.request.UpdateBanAppealRequest;
import com.teosgame.ban.banapi.model.response.BanAppealResponse;
import com.teosgame.ban.banapi.persistence.BanAppealRepository;
import com.teosgame.ban.banapi.util.BanUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BanAppealService {

    private final BanUtils utils;
    private final BanAppealRepository repository;
    
    public BanAppealResponse createBanAppeal(CreateBanAppealRequest request) 
        throws BadRequestException, NotFoundException {
        String twitchUserName = SecurityContextHolder.getContext()
            .getAuthentication().getName();
        
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

            if (!previous.getBanStatus().equals(BanStatus.RESUBMISSION_REQUIRED.toString())) {
                throw new BadRequestException("Previous Ban Appeal does not require a resubmission");
            }
        }

        AppealEntity entity = AppealEntity.builder()
            .twitchUsername(request.getTwitchUsername())
            .discordUsername(request.getDiscordUsername())
            .banType(request.getBanType())
            .banStatus(BanStatus.PENDING.toString())
            .banReason(request.getBanReason())
            .banJustified(request.getBanJustified())
            .appealReason(request.getAppealReason())
            .additionalNotes(request.getAdditionalNotes())
            .additionalData(additionalData)
            .previous(previous)
            .build();

        entity.setCreatedBy(request.getTwitchUsername());

        entity = repository.save(entity);
        
        return BanAppealResponse.builder()
            .appealId(entity.getId().toString())
            .banStatus(BanStatus.PENDING.toString())
            .build();
    }

    public BanAppealResponse updateBanAppeal(String appealId, UpdateBanAppealRequest request) 
        throws BadRequestException, NotFoundException {

        String twitchUserName = SecurityContextHolder.getContext()
            .getAuthentication().getName();
        
        AppealEntity entity = repository.findById(appealId).orElse(null);

        if (entity == null) {
            throw new NotFoundException("Appela with id " + appealId + " does not exist");
        }

        if (!utils.isUserAdmin()) {
            if (!request.getTwitchUsername().equalsIgnoreCase(twitchUserName)) {
                throw new BadRequestException("User submitting ban does not match user name in appeal");
            }
        } else {

        }

        return BanAppealResponse.builder()
            .banStatus(BanStatus.BAN_UPHELD.toString())
            .build();
    }
}
