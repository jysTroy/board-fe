package org.maengle.global.configs;

import lombok.RequiredArgsConstructor;
import org.maengle.member.libs.MemberUtil;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuditorAwareImpl implements AuditorAware<String> {

    private final MemberUtil memberUtil;

    @Override
    public Optional<String> getCurrentAuditor() {
//        String email = memberUtil.isLogin() ? memberUtil.getMember().getEmail() : null;

//        return Optional.ofNullable(email);

        return null;
    }
        // 언젠간 쓸거임(진짜로)
}
