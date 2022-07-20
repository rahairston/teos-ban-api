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
    
    public BanAppealResponse createBanAppeal(CreateBanAppealRequest request) throws BadRequestException {
        String twitchUserName = SecurityContextHolder.getContext()
            .getAuthentication().getName();
        
        if (!request.getTwitchUsername().equalsIgnoreCase(twitchUserName)) {
            throw new BadRequestException("User submitting ban does not match user name in appeal");
        }

        AppealEntity entity = AppealEntity.builder()
            .build();

        entity = repository.save(entity);
        
        return BanAppealResponse.builder()
            .appealId(entity.getId())
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
