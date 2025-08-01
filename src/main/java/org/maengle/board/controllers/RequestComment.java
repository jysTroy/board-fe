package org.maengle.board.controllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestComment {
    private String mode;

    @NotNull
    private Long boardDataSeq; // 게시글 번호
    private Long seq; // 댓글 seq

    @NotBlank
    private String commenter; // 댓글 작성자

    @NotBlank
    private String content; // 댓글 내용
}
