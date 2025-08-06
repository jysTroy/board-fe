package org.maengle.global.libs;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.maengle.banner.entities.Banner;
import org.maengle.banner.service.BannerInfoService;
import org.maengle.file.entities.FileInfo;
import org.maengle.file.services.FileInfoService;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.LocaleResolver;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component("utils")
@RequiredArgsConstructor
public class Utils {

    private final HttpServletRequest request; // 현재 요청 정보
    private final FileInfoService infoService;
    private final LocaleResolver localeResolver;
    private final MessageSource messageSource;
    private final BannerInfoService bannerInfoService;


    public int version() {
        return 1;
    }
    public String keywords() {
        return "";
    }

    public String description() {
        return "";
    }

    // 공통 메시지 가져오기
    public String getMessage(String code) {
        Locale locale = localeResolver.resolveLocale(request);

        return messageSource.getMessage(code, null, locale);
    }

    // 여러 메시지 코드를 한 번에 받아서, 해당 코드에 매핑된 다국어 메시지를 List 형태로 변환 처리
    public List<String> getMessages(String[] codes) {
        ResourceBundleMessageSource ms = (ResourceBundleMessageSource) messageSource;
        ms.setUseCodeAsDefaultMessage(false);
        try {
            return Arrays.stream(codes)
                    .map(c -> {
                        try {
                            return getMessage(c);
                        } catch (Exception e) {}
                        return "";
                    }).filter(s -> !s.isBlank()).toList();
        } finally {
            ms.setUseCodeAsDefaultMessage(true);
        }
    }

    /* 검증 실패 메세지 처리 */
    public Map<String, List<String>> getErrorMessages(Errors errors) {
        // 필드별 검증 실패 메세지  - rejectValue, 커맨드 객체 검증(필드)
        Map<String, List<String>> messages = errors.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, f -> getMessages(f.getCodes()), (v1, v2) -> v2));
        // 글로벌 검증 실패 메세지 - reject
        List<String> gMessages = errors.getGlobalErrors()
                .stream()
                .flatMap(g -> getMessages(g.getCodes()).stream()).toList();

        if (!gMessages.isEmpty()) {
            messages.put("global", gMessages);
        }

        return messages;
    }

    // 썸네일 이미지 출력 HTML 태그 생성 (seq,width,height,addClass 여부 입력 받음)
    public String printThumb(Long seq, int width, int height, String addClass, boolean crop) {
        String url = null;
        try {
            FileInfo item = infoService.get(seq); // 파일 존재 여부
            long folder = seq % 10L; // 폴더 구조화 대비
            url = String.format("%s/file/thumb?seq=%s&width=%s&height=%s&crop=true",
                    request.getContextPath(), seq, width, height);
        }   catch (Exception e) {
            e.printStackTrace();
        }

        /*
         * url이 비어 있다면 기본 이미지 경로로 설정
         * 기본 이미지 추후 추가
         */
        url = StringUtils.hasText(url) ? url : request.getContextPath() + "/common/images/no_image.jpg";

        /*
         * <img> 태그 생성
         *  class 속성: image-파일 번호 + (추가 style.css 클래스가 있다면 공백 붙여서 추가)
         */
        return String.format("<img src='%s' class='%s%s'>",
                url,
                "image-" + seq,
                StringUtils.hasText(addClass) ? " " + addClass : "");
    }

    // 썸네일 출력 오버로딩 메서드 (crop 기본값 true)
    public String printThumb(Long seq, int width, int height, String addClass) {
        return printThumb(seq, width, height, addClass, true);
    }

    // 썸네일 출력 오버로딩 메서드 (추가 CSS class 없음)
    public String printThumb(Long seq, int width, int height) {
        return printThumb(seq, width, height, null);
    }

    // 썸네일 출력 오버로딩 메서드 (기본 크기 100 X 100, class 없음)
    public String printThumb(Long seq) {
        return printThumb(seq, 100, 100);
    }

    /*
     * 이미지가 없는 경우 기본 이미지 출력용 <ima> 태그 반환
     * 웹 애플리케이션의 컨텍스트 경로 + 기본 이미지 경로
     * 기본 이미지 추후 추가
     */
    public String printNoImage() {
        String url = request.getContextPath() + "/common/images/no_image.jpg";

        return String.format("<img src='%s'>", url);
    }

    public String getUrl(String url) {
        String protocol = request.getScheme(); // http 또는 https
        String domain = request.getServerName(); // 도메인명
        int _port = request.getServerPort(); // 포트 번호

        // 80, 443은 URL에 안써도 되므로 생략, 그 외 포트는 명시적으로 붙이기
        String port = List.of(80, 443).contains(_port) ? "" : ":" + _port;

        return String.format("%s://%s%s%s%s",
                protocol, domain, port, request.getContextPath(), url);
    }

    // 이름으로 파라미터 값 쉽게 가져오기 위해서 써놨습니다.
    public String getParam(String name) {
        return request.getParameter(name);
    }

    // 배너 목록
    public List<Banner> getBanners(String groupCode) {
        return bannerInfoService.getList(groupCode);
    }

    /*
    * 배너 페이지 설정
    * 0 이하 정수인 경우 1이상 정수로 대체
    */
    public static int onlyPositiveNumber(int num, int replace) {
        return num < 1 ? replace : num;
    }

    /**
     * 줄개행 문자인 \n 또는 \r\n를 <br>로 변환
     */
    public String nl2br(String text) {
        return text.replaceAll("\r", "")
                .replaceAll("\n", "<br>");
    }

    /***
     *
     * 패스워드 초기화 후 보낼 비밀번호 생성
     */
    public String randomChars() {
        return randomChars(8);
    }

    public String randomChars(int length) {
        // 알파벳 생성
        Stream<String> alphas = IntStream.concat(IntStream.rangeClosed((int)'a', (int)'z'), IntStream.rangeClosed((int)'A', (int)'Z')).mapToObj(s -> String.valueOf((char)s));

        // 숫자 생성
        Stream<String> nums = IntStream.range(0, 10).mapToObj(String::valueOf);

        // 특수문자 생성
        Stream<String> specials = Stream.of("~", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_", "+", "-", "=", "[", "{", "}", "]", ";", ":");

        List<String> chars = Stream.concat(Stream.concat(alphas, nums), specials).collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(chars);

        return chars.stream().limit(length).collect(Collectors.joining());
    }

    /**
     * 태그를 제거한 문자열로 처리
     */
    public String stripTags(String text) {
        return text.replaceAll("<p[^>]*>(.*?)<\\/p>", "\n")
                .replaceAll("<[^>]*>", "")
                .replaceAll("&nbsp", " ");
    }
}