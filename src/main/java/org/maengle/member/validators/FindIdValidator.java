package org.maengle.member.validators;

import lombok.RequiredArgsConstructor;
import org.maengle.member.controllers.RequestFindId;
import org.maengle.member.repositories.MemberRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class FindIdValidator implements Validator {

    private final MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestFindId.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RequestFindId form = (RequestFindId) target;
        String email = form.email();

        if(StringUtils.hasText(email) && !memberRepository.existsByEmail(email)){
            errors.reject("NotFound.member");
        }
    }
}
