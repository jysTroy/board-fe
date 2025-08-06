package org.maengle.file.services;

import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.maengle.file.controllers.RequestThumb;
import org.maengle.file.entities.FileInfo;
import org.maengle.global.configs.FileProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.Objects;

@Lazy
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(FileProperties.class)
public class ThumbnailService {

    private final FileInfoService infoService;
    private final FileProperties properties;

    public String create(RequestThumb form) {
        Long seq = form.getSeq(); // 파일 고유 번호
        String url = form.getUrl(); // seq가 없을시 사용할 원격 URL
        int width = Math.max(form.getWidth(), 50); // 최소 크기를 50
        int height = Math.max(form.getHeight(), 50);
        boolean crop = form.isCrop(); // 크롭 여부

        String thumbPath = getThumbPath(seq, url, width, height, crop);
        File file = new File(thumbPath);
        if (file.exists()) { // 이미 썸네일이 있다면
            return thumbPath; // 바로 경로 반환
        }

        try {
            if (seq != null && seq > 0L) { // seq 기준 썸네일 생성
                FileInfo item = infoService.get(seq); // DB에서 파일 정보 가져오기
                Thumbnails.Builder<File> builder = Thumbnails.of(item.getFilePath())
                        .size(width, height); // 원본파일
                if (crop) {
                    builder.crop(Positions.CENTER); // 크롭이 필요하다면 중앙 부분 크롭
                }

                builder.toFile(thumbPath); // 썸네일 이미지 지정 경로에 저장

            } else if (StringUtils.hasText(url)) {

            }
        } catch (Exception e) { // 썸네일 생성 중 에러가 나면 stacktrace
            e.printStackTrace();
        }

        return thumbPath; // 생성 썸네일 경로 반환
    }

    // 썸네일 경로 만들기 위한 입력값 받는 메서드
    public String getThumbPath(Long seq, String url, int width, int height, boolean crop) {
        String basePath = properties.getPath() + "/thumbs"; // 저장 될 기본 경로 지정

        String thumbPath = ""; // thumbPath에 담기
        if (seq != null && seq > 0L) { // seq가 존재(DB에 있다면) 그 기준으로 경로 생성
            FileInfo item = infoService.get(seq); // infoService로 DB조회
            String folder = infoService.folder(seq); // 해당 파일이 저장 된 서브 폴더 이름을 계산
            File file = new File(basePath + "/" + folder); // 썸네일이 저장 될 폴더의 전체 경로를 File 객체로 생성
            if (!file.exists() || !file.isDirectory()) {
                file.mkdirs(); // 해당 폴더가 존재하지 않거나 폴더가 아니면 새로 생성
            }

            // 문자열 포맷으로 경로 만들기
            thumbPath = basePath + String.format("/%s/%s_%s_%s_%s%s", folder, width, height, crop, seq, Objects.requireNonNullElse(item.getExtension(), ""));
        }

        return thumbPath; // 계산한 썸네일 경로 문자열 반환
    }

    /*
     * 파일 등록 번호 기준의 썸네일 서버 경로 seq, width, height
     * seq 기준 썸네일 경로 반환
     */
    public String getThumbPath(Long seq, int width, int height, boolean crop) {
        return getThumbPath(seq, null, width, height, crop);
    }
}
