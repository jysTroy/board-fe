package org.maengle.mypage.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {

    @GetMapping
    public String index() {

        return "front/mypage/index";
    }

    @GetMapping("/profile")
    public String profile() {

        return null;
    }

    @GetMapping("/board")
    public String myBoard() {

        return null;
    }

    @GetMapping("/resign")
    public String resign() {
        return null;
    }
}
