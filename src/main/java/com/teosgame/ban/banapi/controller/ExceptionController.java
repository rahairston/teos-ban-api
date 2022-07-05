package com.teosgame.ban.banapi.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.teosgame.ban.banapi.client.model.response.ErrorResponse;
import com.teosgame.ban.banapi.exception.BaseException;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    Logger logger = LoggerFactory.getLogger(ExceptionController.class);
    
    @ExceptionHandler(BaseException.class)
    public final ResponseEntity<ErrorResponse> handleBaseException
            (BaseException ex, WebRequest request) {
        String message = ex.getMessage();
        JSONObject responseBody = null;
        if (ex.getResponseBody() != null) {
            try {
                responseBody = new JSONObject(ex.getResponseBody().trim());
                message += ": " + responseBody.getString("message");
            } catch (JSONException err){
                logger.error("Error Parsing JSON for responseBody or finding message: " + ex.getResponseBody());
            }
        }
        ErrorResponse error = new ErrorResponse(message, ex.getCode().value());
        return new ResponseEntity<>(error, ex.getCode());
    }
}
