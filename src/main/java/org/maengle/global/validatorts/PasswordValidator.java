package org.maengle.global.validatorts;

public interface PasswordValidator {

    // 알파벳 포함
    default boolean checkAlphabet(String password) {
        return password.matches(".*[a-zA-Z].*");
    }

    // 숫자 포함
    default boolean checkNumber(String password) {
        return password.matches(".*\\d.*");
    }

    // 숫자, 알파벳, 한글 제외한 문자 == 특수문자 포함
    default boolean checkSectionSign(String password) {
        return password.matches(".*[^0-9a-zA-Zㄱ-ㅎ가-힣].*");
    }


    // 모두 포함 여부 (복잡성 체크용으로 만들어놨습니다.)
    default boolean isValidPassword(String password) {
        return checkAlphabet(password) && checkNumber(password) && checkSectionSign(password);
    }
}
