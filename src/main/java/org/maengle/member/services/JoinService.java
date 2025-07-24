package org.maengle.member.services;

import lombok.RequiredArgsConstructor;
import org.maengle.member.controllers.RequestJoin;
import org.maengle.member.entities.Member;
import org.maengle.member.repositories.MemberRepository;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Lazy
@Service
@RequiredArgsConstructor
public class JoinService {

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository repository;

    // pw BCrypt 해시화
    // mobile 형식 통일화
    public void process(RequestJoin form){
        String password = form.getPassword();
        String mobile = form.getMobile();

        String hash = StringUtils.hasText(password) ? passwordEncoder.encode(password) : null;
        if (StringUtils.hasText(mobile)){
            mobile = mobile.replaceAll("\\D", "");
        }

        Member member = modelMapper.map(form, Member.class);

        // pw : 해시 | mobile : 형식 통일 후 값 적용
        member.setPassword(hash);
        member.setMobile(mobile);

        member.setCredentialChangedAt(LocalDateTime.now());

        repository.saveAndFlush(member);
    }
}
