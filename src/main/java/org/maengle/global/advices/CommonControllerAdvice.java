package org.maengle.global.advices;

import org.maengle.global.annotations.ApplyCommonController;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice(annotations = ApplyCommonController.class)
public class CommonControllerAdvice {

}
