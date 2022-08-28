package com.teosgame.ban.banapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.teosgame.ban.banapi.exception.ForbiddenException;
import com.teosgame.ban.banapi.exception.NotFoundException;
import com.teosgame.ban.banapi.model.request.BannedBysRequest;
import com.teosgame.ban.banapi.service.BannedByService;

@RestController
@RequestMapping("/appeals/{appealId}/bannedBy")
public class BannedByController {
  
  @Autowired
  private BannedByService service;

  @PutMapping(value = "/", produces = "application/json")
  public ResponseEntity<Void> updateAllBannedBy(@PathVariable String appealId,
      @Validated @RequestBody BannedBysRequest request) 
      throws ForbiddenException, NotFoundException {
        service.updateAllBannedBy(appealId, request);
        return ResponseEntity.noContent().build();
  }
}
