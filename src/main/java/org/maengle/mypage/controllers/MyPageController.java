package org.maengle.mypage.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {
    // 아래 index 사용하기 위한 tpl 선언이 안되어 있어서 미리 지정만 했습니다.
//    private final Utils utils;

    @GetMapping
    public String index() {
        return "front/mypage/index";
    }
}
