package org.maengle.member.controllers;

import lombok.Data;

import java.util.List;

@Data
public class RequestLogin {
    private String userId;
    private String password;
    private boolean autoLogin;
    private String redirectUrl;
    private List<String> fieldErrors;
    private List<String> globalErrors;
}
