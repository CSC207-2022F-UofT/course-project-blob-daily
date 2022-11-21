package com.backend.error.handlers;

import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHandler {

    public static ResponseEntity<Object> packageErrorResponse(Exception exception, HttpStatus status) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("Error", exception.getClass().getName());
        jsonObject.put("Message", exception.getMessage());

        return new ResponseEntity<Object>(jsonObject, status);
    }
}
