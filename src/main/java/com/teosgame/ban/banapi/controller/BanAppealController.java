package com.teosgame.ban.banapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teosgame.ban.banapi.exception.BadRequestException;
import com.teosgame.ban.banapi.exception.InvalidTokenException;
import com.teosgame.ban.banapi.exception.NotFoundException;
import com.teosgame.ban.banapi.model.request.CreateBanAppealRequest;
import com.teosgame.ban.banapi.model.response.BanAppealResponse;
import com.teosgame.ban.banapi.service.BanAppealService;

@RestController
@RequestMapping("/appeal")
public class BanAppealController {

    @Autowired
    private BanAppealService service;
    
    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<Void> getBanAppeals() throws InvalidTokenException {
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/", produces = "application/json")
    public ResponseEntity<BanAppealResponse> creatBanAppeal(@RequestBody @Validated CreateBanAppealRequest request) 
        throws BadRequestException, NotFoundException {
        return ResponseEntity.ok(service.createBanAppeal(request));
    }
}
