package com.teosgame.ban.banapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teosgame.ban.banapi.exception.BadRequestException;
import com.teosgame.ban.banapi.exception.ForbiddenException;
import com.teosgame.ban.banapi.exception.NotFoundException;
import com.teosgame.ban.banapi.model.request.EvidenceRequest;
import com.teosgame.ban.banapi.model.response.EvidenceResponse;
import com.teosgame.ban.banapi.service.EvidenceService;

@RestController
@RequestMapping("/appeals/{appealId}/evidence")
public class EvidenceController {

    @Autowired
    private EvidenceService service;

    @GetMapping(value = "/{evidenceId}", produces = "application/json")
    public ResponseEntity<EvidenceResponse> getEvidencebyId(@PathVariable String appealId, @PathVariable String evidenceId) 
        throws NotFoundException, ForbiddenException {
          return ResponseEntity.ok(service.getEvidence(appealId, evidenceId));
    }

    @PutMapping(value = "/{evidenceId}", produces = "application/json")
    public ResponseEntity<EvidenceResponse> updateBanAppealbyId(@PathVariable String appealId,
        @PathVariable String evidenceId, 
        @RequestBody EvidenceRequest request) 
        throws NotFoundException, ForbiddenException, BadRequestException {
        return ResponseEntity.ok(service.updateEvidence(appealId, evidenceId, request));
    }

    @PostMapping(value = "/", produces = "application/json")
    public ResponseEntity<EvidenceResponse> creatBanAppeal(@PathVariable String appealId, @RequestBody @Validated EvidenceRequest request) 
        throws BadRequestException, ForbiddenException, NotFoundException {
        return ResponseEntity.ok(service.createEvidence(appealId, request));
    }

    @DeleteMapping(value = "/{evidenceId}", produces = "application/json")
    public ResponseEntity<EvidenceResponse> deleteAppeal(@PathVariable String appealId, @PathVariable String evidenceId) 
        throws NotFoundException, ForbiddenException, BadRequestException {
        return ResponseEntity.ok(service.deleteEvidence(appealId, evidenceId));
    }
}
