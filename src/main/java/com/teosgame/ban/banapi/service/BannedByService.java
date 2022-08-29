package com.teosgame.ban.banapi.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.teosgame.ban.banapi.exception.ForbiddenException;
import com.teosgame.ban.banapi.exception.NotFoundException;
import com.teosgame.ban.banapi.model.entity.AppealEntity;
import com.teosgame.ban.banapi.model.entity.BannedByEntity;
import com.teosgame.ban.banapi.model.request.BannedByRequest;
import com.teosgame.ban.banapi.model.request.BannedBysRequest;
import com.teosgame.ban.banapi.persistence.BanAppealRepository;
import com.teosgame.ban.banapi.util.BanUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BannedByService {

  private final BanUtils utils;
  private final BanAppealRepository repository;

  Logger logger = LoggerFactory.getLogger(BannedByService.class);

  public void updateAllBannedBy(String appealId, BannedBysRequest request) throws ForbiddenException, NotFoundException {
      if (!utils.isUserAdmin()) {
          logger.error("User {} attempted to modify banned by data", SecurityContextHolder.getContext()
              .getAuthentication().getName());
          throw new ForbiddenException("Only admins can get Banned By data");
      }

      AppealEntity entity = repository.findById(appealId).orElse(null);

      if (entity == null) {
          throw new NotFoundException("Appeal with ID " + appealId + " not found");
      }

      List<BannedByEntity> removingEntities = entity.getBannedBy().stream().filter(item -> {
          BannedByRequest req = getRequestFromListById(request.getRequest(), item.getId());
          if (req != null && req.getAction() != null && req.getAction().isUpdating()) {
              item.setName(req.getName());
              item.setBanDate(req.getBanDate());
              return false;
          } else if (req != null && req.getAction() != null && req.getAction().isDeleting()) {
              return true;
          } else {
              return false;
          }
      }).collect(Collectors.toList());

      removingEntities.forEach(item -> entity.removeBannedBy(item));

      request.getRequest().stream().forEach(item -> {
          if (item.getAction() != null && item.getAction().isCreating()) {
              BannedByEntity bannedByEntity = new BannedByEntity(item.getName(), item.getBanDate(), entity);
              entity.addBannedBy(bannedByEntity);
          }
      });

      repository.save(entity);
  }

  private BannedByRequest getRequestFromListById(List<BannedByRequest> requests, String id) {
      return requests.stream().filter(req -> req.getBannedById() != null && req.getBannedById().equalsIgnoreCase(id)).findAny().orElse(null);
  }
}
