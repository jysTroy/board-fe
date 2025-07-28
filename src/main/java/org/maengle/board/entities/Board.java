package org.maengle.board.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.maengle.global.entities.BaseEntity;
import org.maengle.member.constants.Authority;

import java.io.Serializable;

@Data
@Entity
public class Board extends BaseEntity implements Serializable {

    @Id
    @Column(length=45)
    private String bid;

    @Column(length=100, nullable = false)
    private String name;

    private int rowsForPage;

    private int pageCount;

    private String category;

    private boolean active;
    private boolean editor;
    private boolean imageUpload;
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
