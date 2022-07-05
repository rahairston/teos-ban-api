package com.teosgame.ban.banapi.client.model.response;

import java.io.Serializable;
// import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) 
public class ErrorResponse implements Serializable {
    private String message;
    // private Map<String, Object> responseBody;
    private int status;
}
