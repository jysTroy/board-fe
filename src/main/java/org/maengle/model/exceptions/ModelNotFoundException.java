package org.maengle.model.exceptions;

import org.maengle.global.exceptions.NotFoundException;

public class ModelNotFoundException extends NotFoundException {
    public ModelNotFoundException() {
        super("NotFound.model");
    }
}
