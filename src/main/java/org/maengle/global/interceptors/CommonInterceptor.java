package org.maengle.global.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.maengle.board.services.configs.BoardConfigInfoService;
import org.maengle.member.libs.MemberUtil;
import org.maengle.model.services.ModelViewService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CommonInterceptor implements HandlerInterceptor {

    private final MemberUtil memberUtil;
    private final ModelViewService modelViewService;
    private final BoardConfigInfoService boardConfigInfoService;
    private Map<String, List<String>> categories;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        clearSocialToken(request);

        // true 값이 반환되어만 통과 -> 별다른 기능을 작성하지 않은 상태여서 true값을 반환
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            // 로그인 회원 정보 modelAndView에 담기
            modelAndView.addObject("isLogin", memberUtil.isLogin());
            modelAndView.addObject("isAdmin", memberUtil.isAdmin());
            modelAndView.addObject("loggedMember", memberUtil.getMember());
            // 로그인 했을때 이미지를 modelAndView에 담기
            if (memberUtil.isLogin()) {
                modelAndView.addObject("profile", memberUtil.getMember().getProfileImage());
            }

            // AI 모델 분류값 유지 처리
            processModelCategory(modelAndView);

            // 게시판 메뉴 목록 처리
            processBoardMenus(modelAndView);
        }
    }

    private void clearSocialToken(HttpServletRequest request){
        String url = request.getRequestURI();

        if(!url.contains("/member/join")){
            HttpSession session = request.getSession();
            session.removeAttribute("socialType");
            session.removeAttribute("socialToken");
        }
    }

    /**
     * AI 모델 분류값 유지 처리
     *
     * @param mv
     */
    private void processModelCategory(ModelAndView mv) {
        Map<String, List<String>> data = modelViewService.getCategories();
        this.categories = data;

        List<String> categories = new ArrayList<>(data.keySet()); // 대분류

        mv.addObject("categories", categories);
    }

    /**
     * 하위 분류 조회
     *
     * @param category
     * @return
     */
    public List<String> getSubCategories(String category) {
        return categories.getOrDefault(category, List.of());
    }

    /**
     * 게시판 목록 메뉴
     *
     * @param mv
     */
    private void processBoardMenus(ModelAndView mv) {
        mv.addObject("boardMenus", boardConfigInfoService.getBoardList());
    }
}