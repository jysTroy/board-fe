package org.maengle.main.controllers;

import lombok.RequiredArgsConstructor;
import org.maengle.global.libs.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {

    private final Utils utils;

    @ModelAttribute("pageTitle")
    public String pageTitle() {
        return utils.getMessage("맹글맹글");
    }

    @ModelAttribute("addCss")
    public List<String> addCss() {
        return List.of("main/style");
    }

    @ModelAttribute("addScript")
    public List<String> addScript() {
        return List.of("main/common");
    }

    @GetMapping
    public String mainPage(){
        return "front/main/main";
    }
}
