package org.maengle.admin.board.controllers;

import org.maengle.admin.global.controllers.CommonController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/board")
public class AdminBoardController extends CommonController {

    @Override
    @ModelAttribute("mainCode")
    public String mainCode() {
        return "board";
    }
}
