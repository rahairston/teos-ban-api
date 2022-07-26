package com.teosgame.ban.banapi.validators;

import org.springframework.stereotype.Component;

import com.teosgame.ban.banapi.exception.BadRequestException;
import com.teosgame.ban.banapi.exception.ForbiddenException;
import com.teosgame.ban.banapi.exception.NotFoundException;
import com.teosgame.ban.banapi.model.entity.AppealEntity;
import com.teosgame.ban.banapi.model.request.CreateBanAppealRequest;
import com.teosgame.ban.banapi.model.request.UpdateBanAppealRequest;
import com.teosgame.ban.banapi.util.BanUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BanAppealValidator {

    private final BanUtils utils;

    public void validateGetRequest(AppealEntity entity, String appealId, String twitchUsername)
        throws ForbiddenException, NotFoundException {
        if (entity == null) {
            throw new NotFoundException("Appeal with id " + appealId + " does not exist");
        }

        if (!utils.isUserAdmin()) {
            if (!entity.getTwitchUsername().equalsIgnoreCase(twitchUsername)) {
                throw new ForbiddenException("User does not own this appeal");
            }
        } 
    }
    
    public void validateCreateRequest(CreateBanAppealRequest request, 
        String twitchUsername, int existingAppealCount, AppealEntity previous)
            throws BadRequestException, NotFoundException {
        if (!(utils.isUserAdmin() || request.getTwitchUsername().equalsIgnoreCase(twitchUsername))) {
            throw new BadRequestException("User submitting ban does not match user name in appeal");
        }

        if (existingAppealCount > 0) {
            throw new BadRequestException("User cannot have multiple Pending or Reviewing Appeals Submitted.");
        }

        // Ignore add ons unless they are resubmitting
        if (request.getPreviousAppealId() != null) {
            if (previous == null) {
                throw new NotFoundException("Previous Ban Appeal with id " + request.getPreviousAppealId() + " not found");
            }

            if (!previous.getJudgement().isResubmittable()) {
                throw new BadRequestException("Previous Ban Appeal does not allow a resubmission");
            }
        }
    }

    public void validateUpdateRequest(UpdateBanAppealRequest request, AppealEntity entity, String appealId, String twitchUsername) 
        throws BadRequestException, NotFoundException, ForbiddenException {
        if (entity == null) {
            throw new NotFoundException("Appeal with id " + appealId + " does not exist");
        }

        if (!utils.isUserAdmin()) {
            if (!entity.getTwitchUsername().equalsIgnoreCase(twitchUsername)) {
                throw new BadRequestException("User updating ban does not match user name in appeal");
            }  

            if (!entity.getJudgement().getStatus().isPending()) {
                throw new ForbiddenException("User cannot edit Appeal once evidence has been submitted.");
            }
        } else {
            if (request.getAdminNotes() != null) {
                entity.setAdminNotes(request.getAdminNotes());
            }
        }
    }

    public void validateDeleteRequest(AppealEntity entity, String appealId, String twitchUsername)
        throws BadRequestException, NotFoundException, ForbiddenException {
        if (entity == null) {
            throw new NotFoundException("Appeal with id " + appealId + " does not exist");
        }

        if (!utils.isUserAdmin()) {
            if (!entity.getTwitchUsername().equalsIgnoreCase(twitchUsername)) {
                throw new BadRequestException("User deletiong ban does not match user name in appeal");
            }  

            if (!entity.getJudgement().getStatus().isPending()) {
                throw new ForbiddenException("User cannot delete Appeal once evidence has been submitted.");
            }
        } 
    }
}
