package org.maengle.board.validators;

import org.maengle.board.controllers.RequestComment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CommentValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestComment.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.hasErrors()) return;

        RequestComment form = (RequestComment) target;
        String mode = StringUtils.hasText(form.getMode()) ? form.getMode() : "comment_write";

        // 댓글 수정인 경우, 댓글 등록번호인 seq가 필수
        if ("comment_update".equals(mode) && form.getSeq() == null) {
            errors.rejectValue("seq", "NotNull");
        }
    }
}

