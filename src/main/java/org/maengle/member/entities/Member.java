package org.maengle.member.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.maengle.global.entities.BaseEntity;
import org.maengle.member.constants.Gender;

@Data
@Entity
@Table(indexes = {
        @Index(name = "idx_member_created_at", columnList = "createdAt DESC")
})
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String seq;

    @Column(nullable = false, length = 50)
    private String id;

    @Column(length = 65)
    private String password;

    @Column(nullable = false, length = 45)
    private String name;

    // 주민등록번호
    @Column(length = 15)
    private String idNumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false, length = 75)
    private String email;

    @Column(nullable = false, length = 15)
    private String mobile;

    private boolean termsAgree;
}
