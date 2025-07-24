package org.maengle.admin.board;

import org.maengle.admin.global.controllers.CommonController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/board")
public class AdminBoardController extends CommonController {

    @Override
    public String mainCode() {
        return "";
    }
}
