package org.maengle.global.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
// 목록 + 페이징 하나로 묶기 위해서 만들었음
public class ListData<T> {
    private List<T> items; // 실제 데이터 목록
    private Pagination pagination; // 페이지네이션 정보
}
