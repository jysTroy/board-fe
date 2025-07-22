package org.maengle.file.services;

import lombok.RequiredArgsConstructor;
import org.maengle.file.constants.FileStatus;
import org.maengle.file.entities.FileInfo;
import org.maengle.file.repositories.FileInfoRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Lazy
@Service
@RequiredArgsConstructor
public class FileDeleteService {
    private final FileInfoService infoService;
    private final FileInfoRepository repository;

    // 파일 등록번호(seq)로 삭제 처리 (눈에 안보이는 기능)
    public FileInfo deleteProcess(Long seq) {
        FileInfo item = infoService.get(seq);

        // 관리자 권한 체크 코드 필요
        // 현재 email로 체크할지, id로 체크할지 미정 상태

        // 파일 삭제
        File file = new File(item.getFilePath());
        if (file.exists()) {
            file.delete();
        }

        // DB 처리
        repository.delete(item);
        repository.flush();

        return item;

    }


    // 파일 삭제 처리 메서드(눈에 보이는 기능)
    public List<FileInfo> process(String gid, String location) {
        List<FileInfo> items = infoService.getList(gid, location, FileStatus.ALL);
        List<FileInfo> deletedItems = new ArrayList<>();
        for (FileInfo item : items) {
            try {
                deleteProcess(item.getSeq());
                deletedItems.add(item); // 삭제된 파일 정보
            } catch (Exception e) {}
        }

        return deletedItems;
    }

    // 게시글에 첨부된 파일 삭제 (location 지정 없이 gid(그룹)으로 삭제)
    public List<FileInfo> process(String gid) {
        return process(gid, null);
    }

}
