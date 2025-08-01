package org.maengle.member.repositories;

import com.querydsl.core.BooleanBuilder;
import org.maengle.member.entities.Member;
import org.maengle.member.entities.QMember;
import org.maengle.member.social.constants.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String>, QuerydslPredicateExecutor<Member> {
    Optional<Member> findByUserId(String userId);
    Optional<Member> findByEmail(String email);
    boolean existsByUserId(String userId);
    boolean existsByEmail(String email);

    boolean existsBySocialTypeAndSocialToken(SocialType type, String token);
    Member findBySocialTypeAndSocialToken(SocialType type, String token);

    default boolean existsByEmailAndUserId(String email, String userId){
        QMember member = QMember.member;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(member.email.eq(email))
                .and(member.userId.eq((userId)));

        return exists(builder);
    }
}