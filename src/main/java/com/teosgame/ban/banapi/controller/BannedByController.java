package com.teosgame.ban.banapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.teosgame.ban.banapi.exception.ForbiddenException;
import com.teosgame.ban.banapi.exception.NotFoundException;
import com.teosgame.ban.banapi.model.request.BannedBysRequest;
import com.teosgame.ban.banapi.model.response.BannedByResponse;
import com.teosgame.ban.banapi.service.BannedByService;

@RestController
@RequestMapping("/appeals/{appealId}/bannedBy")
public class BannedByController {
  
  @Autowired
  private BannedByService service;
  
  @GetMapping(value = "/", produces = "application/json")
  public ResponseEntity<List<BannedByResponse>> getBannedBy(@PathVariable String appealId) throws NotFoundException, ForbiddenException {
      return ResponseEntity.ok(service.getAppealsBannedBy(appealId));
  }

  @PutMapping(value = "/", produces = "application/json")
  public ResponseEntity<Void> updateAllBannedBy(@PathVariable String appealId,
      @RequestBody BannedBysRequest request) 
      throws ForbiddenException, NotFoundException {
        service.updateAllBannedBy(appealId, request);
        return ResponseEntity.noContent().build();
  }

  @PostMapping(value = "/", produces = "application/json")
  public ResponseEntity<Void> createBannedBy(@PathVariable String appealId,
      @RequestBody @Validated BannedBysRequest request) 
      throws ForbiddenException, NotFoundException {
      service.createAllBannedBy(appealId, request);
      return ResponseEntity.noContent().build();
  }
}
