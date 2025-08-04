package org.example.todo.ai.service.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.example.todo.ai.service.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RootException.class)
    public ResponseEntity<ErrorResponse> handleRootException(RootException ex) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .code(ex.getCode())
                        .title(ex.getTitle())
                        .errors(ex.getErrors())
                        .build(),
                ex.getHttpStatus()
        );
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex) {
        ApiErrorDetails error = ApiErrorDetails.builder()
                .reason("Response Status Exception: " + ex.getReason())
                .build();

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .code(ex.getStatusCode().toString())
                        .title("Request could not be completed")
                        .errors(List.of(error))
                        .build(),
                ex.getStatusCode()
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnrecognizedProperty(HttpMessageNotReadableException ex) {
        String invalidField = ex.getMostSpecificCause().getMessage();

        ApiErrorDetails error = ApiErrorDetails.builder()
                .reason("Message not readable: " + invalidField)
                .build();

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .code("INVALID_REQUEST")
                        .title("Malformed JSON request")
                        .errors(List.of(error))
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ApiErrorDetails> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(e -> ApiErrorDetails.builder()
                        .reason(e.getDefaultMessage())
                        .build())
                .toList();

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .code("INVALID_BODY")
                        .title("Invalid JSON body")
                        .errors(errors)
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwtException(ExpiredJwtException ex) {
        String message = ex.getMessage();

        ApiErrorDetails error = ApiErrorDetails.builder()
                .reason("Expired JWT: " + message)
                .build();

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .code("EXPIRED_JWT")
                        .title("Expired JWT token")
                        .errors(List.of(error))
                        .build(),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        ApiErrorDetails error = ApiErrorDetails.builder()
                .reason("Runtime Exception: " + ex.getMessage())
                .build();

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .code("RUNTIME_EXCEPTION")
                        .title("A runtime exception occurred")
                        .errors(List.of(error))
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException ex) {
        ApiErrorDetails error = ApiErrorDetails.builder()
                .reason(ex.getMessage())
                .build();

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .code("RESOURCE_NOT_FOUND")
                        .title("Requested resource not found")
                        .errors(List.of(error))
                        .build(),
                HttpStatus.NOT_FOUND
        );
    }

    // TODO: remove in production
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ApiErrorDetails error = ApiErrorDetails.builder()
                .reason("Unhandled Exception: " + ex.getClass().getSimpleName())
                .build();

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .code("UNHANDLED_EXCEPTION")
                        .title("Unexpected error occurred")
                        .errors(List.of(error))
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}