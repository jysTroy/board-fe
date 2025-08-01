package org.maengle.member.services;

import lombok.RequiredArgsConstructor;
import org.maengle.global.email.entities.EmailMessage;
import org.maengle.global.email.services.EmailSendService;
import org.maengle.global.libs.Utils;
import org.maengle.member.controllers.RequestFindPw;
import org.maengle.member.entities.Member;
import org.maengle.member.exceptions.MemberNotFoundException;
import org.maengle.member.repositories.MemberRepository;
import org.maengle.member.validators.FindPwValidator;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.HashMap;
import java.util.Map;

@Lazy
@Service
@RequiredArgsConstructor
public class FindPwService {
    private final FindPwValidator validator;
    private final MemberRepository repository;
    private final EmailSendService sendService;
    private final PasswordEncoder encoder;
    private final Utils utils;

    public void process(RequestFindPw form, Errors errors) {
        validator.validate(form, errors);
        if (errors.hasErrors()) { // 유효성 검사 실패시에는 처리 중단
            return;
        }

        // 비밀번호 초기화
        reset(form.email());

    }

    public void reset(String email) {
        /* 비밀번호 초기화 S */
        Member member = repository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

        String newPassword = utils.randomChars(12); // 초기화 비밀번호는 12자로 생성
        member.setPassword(encoder.encode(newPassword));

        repository.saveAndFlush(member);
        /* 비밀번호 초기화 E */

        EmailMessage emailMessage = new EmailMessage(email, utils.getMessage("Email.password.reset"), utils.getMessage("Email.password.reset"));
        Map<String, Object> tplData = new HashMap<>();
        tplData.put("password", newPassword);
        sendService.sendEmail(emailMessage, "pw_reset", tplData);
    }
}
