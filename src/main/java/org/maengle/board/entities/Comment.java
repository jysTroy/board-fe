package org.maengle.board.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.maengle.global.entities.BaseEntity;
import org.maengle.member.entities.Member;

import java.io.Serializable;

@Data
@Entity
public class Comment extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="board_data_seq")
    private BoardData item;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(length = 45, nullable = false)
    private String commenter;

    @Column(length = 65)
    private String guestPw;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(length = 20)
    private String ip;

    private String ua;

    @Transient
    private boolean editable; // 게시글 수정, 삭제 가능 여부(버튼 노출 여부)

    @Transient
    private boolean mine; // 직접 저장한 게시글인지 여부
}
