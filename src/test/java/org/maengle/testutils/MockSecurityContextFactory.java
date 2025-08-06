package org.maengle.testutils;

import org.maengle.member.MemberInfo;
import org.maengle.member.entities.Member;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.time.LocalDateTime;
import java.util.List;

public class MockSecurityContextFactory implements WithSecurityContextFactory<MockMember> {
    @Override
    public SecurityContext createSecurityContext(MockMember annotation) {
        Member member = new Member();
        member.setUserUuid(annotation.uuid());
        member.setEmail(annotation.email());
        member.setPassword(annotation.password());
        member.setName(annotation.name());
        member.setAuthority(annotation.authority());
        member.setMobile(annotation.mobile());
        member.setTermsAgree(true);
        member.setCredentialChangedAt(LocalDateTime.now().plusMonths(1L));

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(member.getAuthority().name()));

        MemberInfo memberInfo = MemberInfo
                .builder()
                .userId(member.getUserId())
                .password(member.getPassword())
                .member(member)
                .authorities(authorities)
                .build();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(memberInfo, member.getPassword(), authorities);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(token);

        return null;
    }
}
