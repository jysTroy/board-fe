package org.maengle.banner.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.maengle.file.entities.FileInfo;
import org.maengle.global.entities.BaseEntity;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = @Index(name = "idx_banner_basic", columnList = "listOrder DESC, createdAt ASC"))
public class Banner extends BaseEntity {
    @Id
    @GeneratedValue
    private Long seq;

    @Column(length = 60, nullable = false)
    private String bName;

    private String bLink;
    private String target = "_self";

    private long listOrder;

    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupCode")
    private BannerGroup bannerGroup;

    @Transient
    private FileInfo bannerImage;
}
