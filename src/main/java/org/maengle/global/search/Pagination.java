package org.maengle.global.search;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@ToString
public class Pagination {
	private int page;
	private int total;
	private int range;
	private int limit;
	private int firstRangePage;
	private int lastRangePage;
	private int prevRangePage;
	private int nextRangePage;
	private int lastPage;
	private String baseUrl;

	// Pagination(10, 50) -> Pagination(10, 50, 0, 0)
	// -> 아래 정의로 인해 Pagination(10, 50, 0, 0, null)
	public Pagination(int page, int total) {
		this(page, total, 0, 0);
	}

	// Pagination(10, 50, 10, 20) -> Pagination(10, 50, 10, 20, null)
	public Pagination(int page, int total, int range, int limit) {
		this(page, total, range, limit, null);
	}

	/**
	 * @param page : 페이지 번호, 1, 2, 3, 4,
	 * @param total : 총 데이터 갯수 (우리는 게시물 갯수)
	 * @param range : 페이지 구간,  [0][1]...[10]  <- range = 10
	 * @param limit : 한 페이지당 보여줄 데이터 갯수
	 */
	public Pagination(int page, int total, int range, int limit, HttpServletRequest request) {
		page = Math.max(page, 1);

		// 설정 이유 : range, limit의 기본값이 10, 20
		range = range < 1 ? 10 : range;
		limit = limit < 1 ? 20 : limit;

		if (total < 1) return;

		/* 변수 설정 S */
		// 전체 페이지 갯수
		int totalPages = (int)Math.ceil(total / (double)limit);

		// 페이지 구간 번호 0 ~ 10 -> 0, 11 ~ 20 -> 1
		int rangeNum = (page - 1) / range;

		// 현재 구간의 첫번째 페이지 번호
		int firstRangePage = rangeNum * range + 1;

		// 현재 구간의 마지막 페이지 번호
		int lastRangePage = firstRangePage + range -1 ;

		// totalPages > lastRangePage
		lastRangePage = Math.min(totalPages, lastRangePage);

		// 이전 구간의 마지막 페이지, 0이면 이전 구간 없음
		int prevRangePage = rangeNum > 0 ? firstRangePage - 1 : 0;

		// 마지막 페이지 구간 번호
		int lastRangeNum = (totalPages - 1) / range;

		// 다음 구간의 첫번째 페이지
		// 마지막 페이지 구간 번호가 rangeNum보다 작으면 0
		int nextRangePage = rangeNum < lastRangeNum ? lastRangePage + 1 : 0;
		/* 변수 설정 E */

		/* 쿼리스트링 처리 S */
		// 기존 쿼리스트링은 유지 page=만 바꾸기 위한 처리
		String qs = request == null ? "" : request.getQueryString();
		String baseUrl = "?";
		if (StringUtils.hasText(qs)) {
			baseUrl += Arrays.stream(qs.split("&"))
					.filter(s -> !s.contains("page="))
					.collect(Collectors.joining("&")) + "&";
		}
		baseUrl += "page=";
		/* 쿼리스트링 처리 E */

		this.page = page;
		this.total = total;
		this.range = range;
		this.limit = limit;
		this.firstRangePage = firstRangePage;
		this.lastRangePage = lastRangePage;
		this.prevRangePage = prevRangePage;
		this.nextRangePage = nextRangePage;
		this.lastPage = totalPages; // 마지막 페이지가 총 페이지수
		this.baseUrl = baseUrl;
	}

	// String 배열의 0번째 - 페이지 번호, 1번째 - ?page=번호와 같은 쿼리스트링([11, ?page=11])
	public List<String[]> getPages() {
		return total < 1 ? List.of() : IntStream.rangeClosed(firstRangePage, lastRangePage)
				.mapToObj(i -> new String[] {"" + i, baseUrl + i})
				.toList();
	}
}
