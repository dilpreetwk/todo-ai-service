package org.example.todo.ai.service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class RootException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -1252237652704742363L;

    private final HttpStatus httpStatus;
    private final List<ApiErrorDetails> errors = new ArrayList<>();
    private final String code;
    private final String title;

    public RootException(@NonNull final HttpStatus httpStatus, final String code, final String title) {
        super("Error " + UUID.randomUUID());
        this.httpStatus = httpStatus;
        this.code = code;
        this.title = title;
    }
}