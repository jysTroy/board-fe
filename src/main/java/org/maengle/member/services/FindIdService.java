package org.maengle.member.services;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.maengle.member.controllers.RequestFindId;
import org.maengle.member.entities.Member;
import org.maengle.member.repositories.MemberRepository;
import org.maengle.member.validators.FindIdValidator;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

@Lazy
@Service
@RequiredArgsConstructor
public class FindIdService {
    private final FindIdValidator validator;
    private final MemberRepository repository;
    private final HttpSession session;

    public void process(RequestFindId form, Errors errors) {
        validator.validate(form, errors);
        if (errors.hasErrors()) { // 유효성 검사 실패시에는 처리 중단
            return;
        }

        String email = form.email();
        Member member = repository.findByEmail(email).orElse(null);

        String userId = member.getUserId();

        if(!StringUtils.hasText(userId)){
            return;
        }
        session.setAttribute("userId", userId);
    }
}
