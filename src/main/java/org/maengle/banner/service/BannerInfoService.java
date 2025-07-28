package org.maengle.banner.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.maengle.admin.banner.controllers.RequestBanner;
import org.maengle.banner.entities.Banner;
import org.maengle.banner.entities.QBanner;
import org.maengle.banner.repositories.BannerRepository;
import org.maengle.file.entities.FileInfo;
import org.maengle.file.services.FileInfoService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.domain.Sort.Order.asc;
import static org.springframework.data.domain.Sort.Order.desc;

@Service
@RequiredArgsConstructor
public class BannerInfoService {
    private final BannerRepository bannerRepository;
    private final FileInfoService fileInfoService;

    public Banner get(Long seq) {
        Banner banner = bannerRepository.findById(seq).orElseThrow();
        // 예외 처리 아직 X

        addBannerInfo(banner);

        return banner;
    }

    public List<Banner> getList(String groupCode) {
        QBanner banner = QBanner.banner;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(banner.bannerGroup.groupCode.eq(groupCode));
        builder.and(banner.active.eq(true)); // 활성화된 배너만

        List<Banner> items = (List<Banner>) bannerRepository.findAll(
                builder,
                Sort.by(desc("listOrder"), asc("createdAt"))
        );

        // 이미지 첨부
        items.forEach(this::addBannerInfo);

        return items;
    }

    public RequestBanner getForm(Long seq) {
        Banner data = get(seq);
        RequestBanner form = new ModelMapper().map(data, RequestBanner.class);
        form.setBannerImage(data.getBannerImage());
        form.setGroupCode(data.getBannerGroup().getGroupCode());
        form.setMode("edit");

        return form;
    }


    private void addBannerInfo(Banner banner) {
        String groupCode = banner.getBannerGroup().getGroupCode();
        List<FileInfo> banners = fileInfoService.getListDone(groupCode, "banner_" + banner.getSeq());
        if (banners != null && !banners.isEmpty()) {
            banner.setBannerImage(banners.get(0));
        }
    }
}
