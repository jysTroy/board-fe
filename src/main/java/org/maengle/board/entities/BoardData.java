package org.maengle.board.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.maengle.global.entities.BaseEntity;

@Data
@Entity
public class BoardData extends BaseEntity {

    @Id
    private Long seq;

    private String gid;

    private String category;

    private String poster;

    private int viewCount;
}
