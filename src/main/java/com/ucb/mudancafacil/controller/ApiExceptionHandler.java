package com.ucb.mudancafacil.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ProblemDetail handleResponseStatus(ResponseStatusException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(ex.getStatusCode(), ex.getReason());
        pd.setTitle("Business Error");
        return pd; // preserva 409
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleNotFound(EntityNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Not Found");
        return pd;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (var err : ex.getBindingResult().getFieldErrors()) {
            String field = err.getField();
            String msg = err.getDefaultMessage();
            // Se quiser incluir rejectedValue: ((FieldError) err).getRejectedValue()
            errors.put(field, msg);
        }
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Validation Error");
        pd.setDetail("One or more fields are invalid.");
        pd.setProperty("errors", errors); // campos -> mensagens
        return pd;
    }

    // Fallback: 500 (NÃO usar 401 aqui!)
    @ExceptionHandler(ErrorResponseException.class)
    public ProblemDetail handleErrorResponse(ErrorResponseException ex) {
        return ex.getBody();
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error");
        pd.setTitle("Server Error");
        return pd;
    }
}
