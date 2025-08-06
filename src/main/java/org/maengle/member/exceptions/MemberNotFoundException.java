package org.maengle.member.exceptions;

import org.maengle.global.exceptions.NotFoundException;

public class MemberNotFoundException extends NotFoundException {
    public MemberNotFoundException() {
        super("NotFound.member");
    }
}
