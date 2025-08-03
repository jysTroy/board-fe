package org.maengle.member.validators;

import lombok.RequiredArgsConstructor;
import org.maengle.member.controllers.RequestFindPw;
import org.maengle.member.repositories.MemberRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class FindPwValidator implements Validator {

    private final MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestFindPw.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        // 이메일 + 회원명 조합으로 조회 되는지 체크
        RequestFindPw form = (RequestFindPw) target;
        String email = form.email();
        String userId = form.userId();

        if (StringUtils.hasText(email) && StringUtils.hasText(userId) && !memberRepository.existsByEmailAndUserId(email, userId)) {
            errors.reject("NotFound.member");
        }
    }
}
