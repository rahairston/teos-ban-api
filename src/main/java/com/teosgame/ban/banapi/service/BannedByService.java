package com.teosgame.ban.banapi.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.teosgame.ban.banapi.exception.ForbiddenException;
import com.teosgame.ban.banapi.exception.NotFoundException;
import com.teosgame.ban.banapi.model.entity.AppealEntity;
import com.teosgame.ban.banapi.model.entity.BannedByEntity;
import com.teosgame.ban.banapi.model.request.BannedByRequest;
import com.teosgame.ban.banapi.model.request.BannedBysRequest;
import com.teosgame.ban.banapi.model.response.BannedByResponse;
import com.teosgame.ban.banapi.persistence.BanAppealRepository;
import com.teosgame.ban.banapi.util.BanUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BannedByService {

  private final BanUtils utils;
  private final BanAppealRepository appealRepository;

  public List<BannedByResponse> getAppealsBannedBy(String appealId) throws ForbiddenException, NotFoundException {
      if (!utils.isUserAdmin()) {
          throw new ForbiddenException("Only admins can get Banned By data");
      }

      AppealEntity entity = appealRepository.findById(appealId).orElse(null);

      if (entity == null) {
        throw new NotFoundException("Appeal with ID "+ appealId + " not found");
      }

      return entity.getBannedBy().stream().map(bannedBy -> {
        return new BannedByResponse(bannedBy);
      }).collect(Collectors.toList());
  }

  public void updateAllBannedBy(String appealId, BannedBysRequest request) throws ForbiddenException, NotFoundException {
      if (!utils.isUserAdmin()) {
          throw new ForbiddenException("Only admins can get Banned By data");
      }

      AppealEntity entity = appealRepository.findById(appealId).orElse(null);

      if (entity == null) {
          throw new NotFoundException("Appeal with ID " + appealId + " not found");
      }

      List<BannedByEntity> bannedByEntities = entity.getBannedBy().stream().filter(item -> {
          BannedByRequest req = getRequestFromListById(request.getRequest(), item.getId());
          if (req != null && req.getAction() != null && req.getAction().isUpdating()) {
              item.setName(req.getName());
              item.setBanDate(req.getBanDate());
              return true;
          } else if (req != null && req.getAction() != null && req.getAction().isDeleting()) {
              return false;
          } else {
              return true;
          }
      }).collect(Collectors.toList());

      request.getRequest().stream().forEach(item -> {
          if (item.getAction() != null && item.getAction().isCreating()) {
              bannedByEntities.add(new BannedByEntity(item.getName(), item.getBanDate(), entity));
          }
      });
      
      entity.setBannedBy(bannedByEntities);

      appealRepository.save(entity);
  }

  public void createAllBannedBy(String appealId, BannedBysRequest request) throws ForbiddenException, NotFoundException {
      if (!utils.isUserAdmin()) {
          throw new ForbiddenException("Only admins can add Banned By data");
      }

      AppealEntity entity = appealRepository.findById(appealId).orElse(null);

      if (entity == null) {
          throw new NotFoundException("Appeal with ID "+ appealId + " not found");
      }

      List<BannedByEntity> bannedByEntities = request.getRequest().stream()
          .map(req -> {
              return new BannedByEntity(req.getName(), req.getBanDate(), entity);
          }).collect(Collectors.toList());

      
      entity.setBannedBy(bannedByEntities);

      appealRepository.save(entity);
  }

  private BannedByRequest getRequestFromListById(List<BannedByRequest> requests, String id) {
      return requests.stream().filter(req -> req.getId() != null && req.getId().equalsIgnoreCase(id)).findAny().orElse(null);
  }
}
