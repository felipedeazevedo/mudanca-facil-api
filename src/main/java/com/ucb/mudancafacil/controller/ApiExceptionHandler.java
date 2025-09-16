package com.ucb.mudancafacil.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

//@RestControllerAdvice
//public class ApiExceptionHandler {
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<Map<String,Object>> handleIllegalArgument(IllegalArgumentException ex) {
//        Map<String,Object> body = Map.of(
//                "error", "VALIDATION",
//                "message", ex.getMessage()
//        );
//        return ResponseEntity.status(HttpStatus.CONFLICT).body(body); // 409
//    }
//}
