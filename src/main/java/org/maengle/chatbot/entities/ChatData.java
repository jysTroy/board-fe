package org.maengle.chatbot.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.maengle.chatbot.constants.ChatbotModel;
import org.maengle.global.entities.BaseEntity;
import org.maengle.member.entities.Member;

@Data
@Entity
public class ChatData extends BaseEntity {
    @Id
    @GeneratedValue
    private Long seq;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ChatbotModel model;

    @Column(length = 45, nullable = false)
    private String roomId;

    @ManyToOne
    private Member member;

    @Column(length = 500, nullable = false)
    private String userMessage; // 사용자가 작성한 메세지

    @Column(length = 500)
    private String sysMessage; // 챗봇이 답변한 메세지

    private String emotion;
}
