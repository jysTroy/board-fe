package org.maengle.admin.member.controllers;

import lombok.RequiredArgsConstructor;
import org.maengle.admin.global.controllers.CommonController;
import org.maengle.member.services.MemberInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/member")
public class AdminMemberController extends CommonController {

    private final MemberInfoService memberInfoService;

    @Override
    @ModelAttribute("mainCode")
    public String mainCode() {
        return "member";
    }

}
