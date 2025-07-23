package org.maengle.global.validatorts;

public interface MobileValidator {

    default boolean checkMobile(String mobile) {
        mobile = mobile.replaceAll("\\D", "");
        String mobilePattern = "^01[016]\\d{3,4}\\d{4}$";
        return mobile.matches(mobilePattern);
    }

}
