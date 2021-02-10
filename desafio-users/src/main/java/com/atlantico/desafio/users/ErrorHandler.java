package com.atlantico.desafio.users;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Slf4j
@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @SuppressWarnings("ConstantConditions")
    @Override
    @ResponseBody
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        val errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .filter(erro -> {
                    erro.getField();
                    return true;
                })
                .collect(toMap(fieldError ->
                                fieldError.getField()
                                        .replaceAll("([^_A-Z])([A-Z])", "$1_$2")
                                        .toLowerCase().replace(".", "_"),
                        FieldError::getDefaultMessage, (a1, a2) -> a1)
                );

        log.warn("errors de validacao: " + errors);

        MessageExceptionResponse body = body(status);
        body.setErrors(errors);

        return new ResponseEntity<>(body, headers, status);
    }

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonPropertyOrder({"timestamp", "status", "error", "code", "message"})
    public static class MessageExceptionResponse implements Serializable, AutoCloseable {

        @JsonProperty(value = "message")
        private String message;

        @JsonProperty(value = "timestamp")
        private String timestamp;

        @JsonProperty(value = "status")
        private Integer status;

        @JsonProperty(value = "error")
        private String error;

        @JsonProperty(value = "errors")
        private Map<String, String> errors;

        public MessageExceptionResponse() {
        }

        @Override
        public void close() {
        }
    }

    public static MessageExceptionResponse body(HttpStatus status) {
        MessageExceptionResponse exceptionResponse = new MessageExceptionResponse();
        exceptionResponse.setTimestamp(dateFormatter());
        exceptionResponse.setStatus(status.value());
        exceptionResponse.setError(status.name());
        return exceptionResponse;
    }

    public static String dateFormatter() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy H:m:s");
        return formatter.format(LocalDateTime.now());
    }
}
