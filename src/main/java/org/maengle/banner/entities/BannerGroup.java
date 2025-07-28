package org.maengle.banner.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.maengle.global.entities.BaseEntity;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class BannerGroup extends BaseEntity {
    @Id
    private String groupCode;

    @Column(length = 80, nullable = false)
    private String groupName;

    private boolean active;
}
