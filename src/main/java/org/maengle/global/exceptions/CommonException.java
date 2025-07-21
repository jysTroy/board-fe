package org.maengle.global.exceptions;

import org.springframework.http.HttpStatus;

public class CommonException extends RuntimeException {
    private final HttpStatus status;

    public CommonException(String message, HttpStatus status) {
        super(message);
        this.status = status;
      // 달님 왔다감
    }
}
