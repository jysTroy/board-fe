package org.maengle.global.email.controllers;

import lombok.RequiredArgsConstructor;
import org.maengle.global.email.services.EmailVerifyService;
import org.maengle.global.rests.JSONData;
import org.maengle.member.repositories.MemberRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class ApiEmailController {
    private final MemberRepository memberRepository;
    private final EmailVerifyService verifyService;

    // "/verify" 요청 시 이메일 인증코드 발급
    @GetMapping("/verify")
    public JSONData<Object> sendVerifyEmail(@RequestParam("email") String email) {
        JSONData<Object> data = new JSONData<>();

        boolean result = verifyService.sendCode(email);
        data.setSuccess(result);

        return data;
    }

    // "/auth_check" 요청 시 인증코드 일치 여부 체크
    @GetMapping("/auth_check")
    public JSONData<Object> checkVerifiedEmail(@RequestParam("authNum") int authNum) {
        JSONData<Object> data = new JSONData<>();

        boolean result = verifyService.check(authNum);
        data.setSuccess(result);

        return data;
    }

    // 이메일 중복 여부 체크 -> joinvalidate는 폼 제출 시 RequestJoin 검토여서 인증 시 중복 여부를 확인하기 위해 별도 검토
    @GetMapping("/email_dup_check")
    public JSONData<Object> duplicateEmailCheck(@RequestParam("email") String email) {
        boolean isExists = memberRepository.existsByEmail(email);

        JSONData<Object> data = new JSONData<>();
        data.setSuccess(isExists);

        return data;
    }
}
