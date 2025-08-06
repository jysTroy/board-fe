package org.maengle.global.validators;

public interface MobileValidator {
    default boolean checkMobile(String mobile) {
        // 숫자만 남기고 그리고 전화번호의 일반적인 패턴과 유사한지 확인해서 T,F로 반환
        mobile = mobile.replaceAll("\\D", "");
        String pattern = "^01[016]\\d{3,4}\\d{4}$";
        return mobile.matches(pattern);
    }
}
