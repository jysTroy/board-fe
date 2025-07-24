package org.maengle.admin.member.controllers;

import lombok.RequiredArgsConstructor;
import org.maengle.admin.global.controllers.CommonController;
import org.maengle.global.search.ListData;
import org.maengle.member.constants.Authority;
import org.maengle.member.controllers.MemberSearch;
import org.maengle.member.entities.Member;
import org.maengle.member.services.MemberInfoService;
import org.maengle.member.services.MemberUpdateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/member")
public class AdminMemberController extends CommonController {

    private final MemberInfoService memberInfoService;
    private final MemberUpdateService updateService;

    @ModelAttribute("authorities")
    public Authority[] authorities() {
        return Authority.values();
    }

    @Override
    @ModelAttribute("mainCode")
    public String mainCode() {
        return "member";
    }

    @GetMapping({"", "/list"})
    public String list(@ModelAttribute MemberSearch search, Model model) {
        commonProcess("list", model);

        ListData<Member> data = memberInfoService.getList(search);

        model.addAttribute("items", data.getItems());
        model.addAttribute("pagination", data.getPagination());

        return "admin/member/list";
    }

    // 컨트롤러 공통 프로세스임
    private void commonProcess(String code, Model model) {
        // code가 비어있으면 기본값 list로 처리한다는거임
        code = StringUtils.hasText(code) ? code : "list";
        String pageTitle = "";

        // code가 list면 회원목록 간다는거임
        if (code.equals("list")) {
            pageTitle = "회원목록";
        }

        // html에 넘겨줄 값 목록이라는 소리임
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("subCode", code);
    }

}
