//package org.maengle.admin.model.controllers;
//
//import lombok.RequiredArgsConstructor;
//import org.maengle.admin.global.controllers.CommonController;
//import org.maengle.file.services.FileInfoService;
//import org.maengle.global.annotations.ApplyCommonController;
//import org.maengle.model.constants.ModelStatus;
//import org.maengle.model.controllers.ModelSearch;
//import org.maengle.model.services.ModelInfoService;
//import org.maengle.model.services.ModelUpdateService;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.util.List;
//
//@Controller
//@ApplyCommonController
//@RequiredArgsConstructor
//@RequestMapping("/admin/model")
//public class ModelController extends CommonController {
//
//	private final ModelUpdateService modelUpdateService;
//	private final ModelInfoService modelInfoService;
//	private final FileInfoService fileInfoService;
//
//
//	@Override
//	@ModelAttribute("mainCode")
//	public String mainCode() {
//		return "model";
//	}
//
//	@ModelAttribute("addCss")
//	public List<String> addCss() {
//		return List.of("product/style");
//	}
//
//	@ModelAttribute("statusList")
//	public ModelStatus[] statusList() {
//		return ModelStatus.values();
//	}
//
//	// 상품 목록, 여기 Model model은 다른거 컨트롤 좌클릭 해보기
//	@GetMapping({"", "/list"})
//	public String list(@ModelAttribute ModelSearch search, Model model) {
//		commonProcess
//	}
//
//
//	private void commonProcess(String code, Model model) {
//		code = StringUtils.hasText(code) ? code : "list";
//	}
//
//}
