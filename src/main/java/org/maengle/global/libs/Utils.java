package org.maengle.global.libs;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class Utils {

    private final HttpServletRequest request;
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;


    public String getMessage(String code) {
        Locale locale = localeResolver.resolveLocale(request);

        return messageSource.getMessage(code, null, locale);
    }
}
