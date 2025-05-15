package br.com.coletaverde.coletaverde.controllers.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.LinkedHashMap;
import java.util.Map;

public class ResultErrorUtil {

 public static Map<String, String> getFieldErrors(BindingResult result) {
        Map<String, String> errors = new LinkedHashMap<>();

        for (FieldError fieldError : result.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return errors;
    }
}