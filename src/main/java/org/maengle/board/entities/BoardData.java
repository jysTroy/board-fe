package org.maengle.board.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.maengle.file.entities.FileInfo;
import org.maengle.global.entities.BaseEntity;
import org.maengle.member.entities.Member;

import java.io.Serializable;
import java.util.List;

@Data
@Entity
public class BoardData extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue
    private Long seq;

    @Column(length=45, nullable = false)
    private String gid;

    @JoinColumn(name="bid")
    @ManyToOne(fetch= FetchType.LAZY)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(length=60)
    private String category;

    @Column(length=60, nullable = false)
    private String poster;

    @Column(nullable = false)
    private String subject;

    @Lob
    @Column(nullable = false)
    private String content;

    private boolean notice; // 공지글 여부

    private int viewCount; // 조회수

    private int commentCount; // 댓글 수

    @Column(length=20)
    private String ip;

    private String ua; // User-Agent 정보, 작성자의 브라우저 정보

    private boolean plainText; // true : 에디터(HTML)를 사용하지 않은 일반 텍스트 게시글,

    @Transient
    private List<FileInfo> editorImages;

    @Transient
    private List<FileInfo> attachFiles;

    @Transient
    private boolean editable; // 게시글 수정,  가능 여부

    @Transient
    private boolean mine; // 내 게시글

}
