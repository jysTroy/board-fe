package org.maengle.board.exceptions;

import org.maengle.global.exceptions.NotFoundException;

public class BoardNotFoundException extends NotFoundException {
    public BoardNotFoundException() {
        // 흉내만 내놓겠습니다.
        super("NotFound.board");
    }
}
