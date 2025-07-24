package org.maengle.member.services;

import lombok.RequiredArgsConstructor;
import org.maengle.file.services.FileInfoService;
import org.maengle.member.MemberInfo;
import org.maengle.member.constants.Authority;
import org.maengle.member.entities.Member;
import org.maengle.member.repositories.MemberRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Lazy
@Service
@RequiredArgsConstructor
public class MemberInfoService implements UserDetailsService {

    private final MemberRepository repository;
    private final FileInfoService fileInfoService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //QMember _member = QMember.member;
        Member member = repository.findByUserId(username).orElseThrow(() -> new UsernameNotFoundException(username));

        Authority authority = Objects.requireNonNullElse(member.getAuthority(), Authority.MEMBER);

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(authority.name()));

        addInfo(member); // 회원 정보 정보 처리

        return MemberInfo.builder()
                .userId(member.getUserId())
                .password(member.getPassword())
                .member(member)
                .authorities(authorities)
                .build();
    }

    /**
     * 회원 정보 추가 처리
     * @param member
     */
    private void addInfo(Member member) {
        member.setProfileImage(fileInfoService.get(member.getGid()));
    }
}