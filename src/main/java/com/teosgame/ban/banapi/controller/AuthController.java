package com.teosgame.ban.banapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teosgame.ban.banapi.exception.BadRequestException;
import com.teosgame.ban.banapi.exception.NotFoundException;
import com.teosgame.ban.banapi.exception.TwitchResponseException;
import com.teosgame.ban.banapi.exception.UnknownException;
import com.teosgame.ban.banapi.exception.UserUnverifiedException;
import com.teosgame.ban.banapi.model.response.TokenResponse;
import com.teosgame.ban.banapi.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    
    @GetMapping(value = "/token", produces = "application/json")
    public ResponseEntity<TokenResponse> getAuthToken(@RequestParam(required = false) String code, @RequestParam(required = false) String refresh) 
        throws TwitchResponseException, UnknownException, NotFoundException, UserUnverifiedException, BadRequestException {
        if (code == null && refresh == null) {
            throw new BadRequestException("One request parameter of \"code\" or \"refresh\" is required");
        }

        if (code != null && refresh != null) {
            throw new BadRequestException("Only One request parameter of \"code\" or \"refresh\" is required");
        }

        return ResponseEntity.ok(authService.getTwitchToken(code, refresh));
    }
}
