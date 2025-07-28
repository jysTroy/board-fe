package org.maengle.admin.banner.controllers;

import lombok.RequiredArgsConstructor;
import org.maengle.banner.entities.Banner;
import org.maengle.banner.service.BannerInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller("adminBannerController")
@RequestMapping("/admin/banner")
@RequiredArgsConstructor
public class BannerController {

    private final BannerInfoService bannerInfoService;

    @ModelAttribute("menuCode")
    public String menuCode() {
        return "banner";
    }


    // 배너 그룹 목록 화면
    @GetMapping
    public String group(Model model) {
        model.addAttribute("pageTitle", "배너그룹");
        return "admin/banner/group";
    }

    // 배너 목록 화면
    @GetMapping("/list/{groupCode}")
    public String bannerList(@PathVariable("groupCode") String groupCode, Model model) {
        List<Banner> items = bannerInfoService.getList(groupCode);
        model.addAttribute("items", items);
        model.addAttribute("pageTitle", "배너목록");
        return "admin/banner/list";
    }

    // 배너 등록 화면
    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("pageTitle", "배너등록");
        return "admin/banner/add";
    }

    // 배너 수정 화면
    @GetMapping("/edit/{seq}")
    public String edit(@PathVariable Long seq, Model model) {
        // 배너 정보 조회 (서비스에 getForm 같은 메서드 있어야 함)
        model.addAttribute("requestBanner", bannerInfoService.getForm(seq));
        model.addAttribute("pageTitle", "배너수정");
        return "admin/banner/edit";
    }
}