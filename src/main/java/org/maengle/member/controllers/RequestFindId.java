package org.maengle.member.controllers;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RequestFindId(
        @NotBlank @Email
        String email
) {}
