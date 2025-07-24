package org.maengle.global.exceptions.script;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AlertRedirectException extends AlertException {
    private final String target;
    private final String url;

    public AlertRedirectException(String message, String url, HttpStatus status, String target) {
        super(message, status);
        this.url = url;
        this.target = target;
    }

    // url 있는데 왜 "self"? -> url을 "현재 페이지"에서 이동하기 위해 target을 self로 지정
    public AlertRedirectException(String message, String url, HttpStatus status) {
        this(message, url, status, "self");
    }

}
