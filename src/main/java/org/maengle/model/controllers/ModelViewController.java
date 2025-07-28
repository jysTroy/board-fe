package org.maengle.model.controllers;

import lombok.RequiredArgsConstructor;
import org.maengle.global.search.ListData;
import org.maengle.model.services.ModelInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/model")
@RequiredArgsConstructor
public class ModelViewController {

	private final ModelInfoService modelInfoService;

	// 상품 목록
	@GetMapping
	public String list(@ModelAttribute ModelSearch search, Model model) {
		String searchType = search.getSearchType();
		System.out.println("선택된 검색 조건:" + searchType);

		ListData<org.maengle.model.entities.Model> data = modelInfoService.getModel(search);
		model.addAttribute("items", data.getItems());
		model.addAttribute("pagination", data.getPagination());

		model.addAttribute("search", search);
		model.addAttribute("pageTitle", "모델 목록");

		return "front/model/list";
	}
}
