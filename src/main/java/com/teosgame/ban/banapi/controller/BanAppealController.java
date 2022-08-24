package com.teosgame.ban.banapi.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.teosgame.ban.banapi.exception.BadRequestException;
import com.teosgame.ban.banapi.exception.ForbiddenException;
import com.teosgame.ban.banapi.exception.NotFoundException;
import com.teosgame.ban.banapi.model.request.CreateBanAppealRequest;
import com.teosgame.ban.banapi.model.response.BanAppealResponse;
import com.teosgame.ban.banapi.model.response.BanAppealsResponse;
import com.teosgame.ban.banapi.service.BanAppealService;

@RestController
@RequestMapping("/appeals")
public class BanAppealController {

    @Autowired
    private BanAppealService service;
    
    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<BanAppealsResponse> getBanAppeals(
        @RequestParam(required = false) String username,
        @RequestParam(required = false, name = "type") String banType,
        @RequestParam(required = false, name = "status") String judgementStatus,
        @RequestParam(required = true, name = "pageCount") int pageCount,
        @RequestParam(required = true, name = "pageSize") int pageSize) throws BadRequestException {
        return ResponseEntity.ok(service.getBanAppeals(username, banType, judgementStatus, pageCount, pageSize));
    }

    @GetMapping(value = "/{appealId}", produces = "application/json")
    public ResponseEntity<BanAppealResponse> getBanAppealbyId(@PathVariable String appealId) 
        throws NotFoundException, ForbiddenException {
        return ResponseEntity.ok(service.getBanAppeal(appealId));
    }

    @PostMapping(value = "/", produces = "application/json")
    public ResponseEntity<Void> creatBanAppeal(@RequestBody @Validated CreateBanAppealRequest request) 
        throws BadRequestException, NotFoundException {
        URI location = UriComponentsBuilder
            .fromPath(service.createBanAppeal(request))
            .build().toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping(value = "/{appealId}", produces = "application/json")
    public ResponseEntity<BanAppealResponse> deleteAppeal(@PathVariable String appealId) 
        throws NotFoundException, ForbiddenException, BadRequestException {
        service.deleteBanAppeal(appealId);
        return ResponseEntity.noContent().build();
    }
}
