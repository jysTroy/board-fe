package org.maengle.admin.global.controllers;

import org.maengle.admin.global.menus.Menu;
import org.maengle.admin.global.menus.Menus;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

public abstract class CommonController {
    // 메인코드(메뉴코드), 관리자가 관리할 각 요소의 controller에 명시 필요
    public abstract String mainCode();

    @ModelAttribute("subMenus")
    public List<Menu> subMenus() {
        return Menus.getMenus(mainCode());
    }
}
