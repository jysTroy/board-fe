package org.maengle.board.exceptions;

import org.maengle.global.exceptions.NotFoundException;

public class CommentNotFoundException extends NotFoundException {
    public CommentNotFoundException() {
        // 흉내만 내놓겠습니다.
        super("NotFound.board");
    }
}