package org.maengle.admin.Model.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.maengle.file.entities.FileInfo;

import java.util.List;

@Data
public class RequestModel {
	private Long seq;

	@NotBlank
	private String mid;

	@NotBlank
	private String name;
	private String category;
	private Long count;

	private String description;

	private List<FileInfo> listImages;
	private List<FileInfo> mainImages;
}
