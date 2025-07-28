package org.maengle.admin.banner.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/banner")
public class BannerController {

    @GetMapping
    public String listBanners() {

        return "admin/banner/list";
    }
}
