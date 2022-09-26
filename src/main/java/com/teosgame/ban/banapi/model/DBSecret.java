package com.teosgame.ban.banapi.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonAutoDetect
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) 
public class DBSecret {
    String username;
    String password;
    String host;
    int port;
    String engine;
}
