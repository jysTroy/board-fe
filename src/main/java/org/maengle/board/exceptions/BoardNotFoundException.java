package org.maengle.board.exceptions;

import org.maengle.global.exceptions.CommonException;
import org.springframework.http.HttpStatus;

public class BoardNotFoundException extends CommonException {
    public BoardNotFoundException(String message) {
        // 아직 AlertException을 안만들어서 CommonException으로 흉내만 내놓겠습니다.
        super(message, HttpStatus.NOT_FOUND);
    }
}
