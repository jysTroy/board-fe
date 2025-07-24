package org.maengle.global.exceptions.script;

import org.maengle.global.exceptions.CommonException;
import org.springframework.http.HttpStatus;

public class AlertException extends CommonException {
    public AlertException(String message, HttpStatus status) {
        super(message, status);
    }

    // status를 고정하고 예외를 출력하는 기능도 넣을 수 있음
}
