package org.maengle.board.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;
import org.maengle.global.entities.BaseEntity;
import org.maengle.member.constants.Authority;

@Data
@Entity
public class Board extends BaseEntity {

    @Id
    private String bid;

    private String name;

    private int rowsForPage;

    private int pageCount;

    private String category;

    private boolean active;
    private boolean attachFile;
    private boolean comment;

    @Enumerated(EnumType.STRING)
    private Authority listAuthority;
    // 목록 권한, MEMBER - 회원, ADMIN - 관리자

    @Enumerated(EnumType.STRING)
    private Authority viewAuthority;

    @Enumerated(EnumType.STRING)
    private Authority writeAuthority;

    @Enumerated(EnumType.STRING)
    private Authority commentAuthority;

}
