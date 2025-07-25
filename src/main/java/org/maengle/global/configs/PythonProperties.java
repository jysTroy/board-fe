package org.maengle.global.configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "python.path")
public class PythonProperties {

    // 각 피씨에 있는 파이썬 폴더안 Scripts 위치
    private String base;

    // 각 피씨에 있는 파이썬 폴더 위치
    private String chatbot;
}
