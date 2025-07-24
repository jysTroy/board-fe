package org.maengle.global.exceptions.script;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AlertBackException extends AlertException {
  private final String target;

  public AlertBackException(String message, HttpStatus status, String target) {
    super(message, status);
    this.target = target;
  }

  // 현재 페이지로 다시 돌아가기 위해 "self"
  public AlertBackException(String message, HttpStatus status) {
    this(message, status, "self");
  }

}
