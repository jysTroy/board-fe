package org.maengle.chatbot.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.maengle.chatbot.entities.ChatData;
import org.maengle.global.configs.PythonProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.stream.Collectors;

@Lazy
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(PythonProperties.class)
public class ChatService {
    private final PythonProperties pythonProperties;
    private final WebApplicationContext webApplicationContext;

    private final ObjectMapper objectMapper;

    public ChatData chatProcess(String message) {

        try {

            // 강사님이 mac 쓰실거라 넣어놨어요
            boolean isProduction = Arrays.stream(webApplicationContext.getEnvironment().getActiveProfiles()).anyMatch(s -> s.equals("prod") || s.equals("mac"));

            String activationCommand = null, pythonPath = null;
            if (isProduction) { // 리눅스
                activationCommand = String.format("%s/activate", pythonProperties.getBase());
                pythonPath = pythonProperties.getBase() + "/python";
                } else { // 윈도우
                    activationCommand = String.format("%s/activate.bat", pythonProperties.getBase());
                    pythonPath = pythonProperties.getBase() + "/python.exe";
                }

                // 가상환경 활성화
                ProcessBuilder builder = isProduction ? new ProcessBuilder("/bin/sh", activationCommand) : new ProcessBuilder(activationCommand);
                Process process = builder.start();
                if (process.waitFor() == 0) {
                    builder = new ProcessBuilder(pythonPath, pythonProperties.getChatbot() + "/chatbot.py", message); // .py 이름 확인해서 바꿔 주세요
                    process = builder.start();
                    int statusCode = process.waitFor();
                    if (statusCode == 0) {
                        String json = process.inputReader().lines().collect(Collectors.joining());

                        return objectMapper.readValue(json, ChatData.class);
                } else {
                    System.out.println("statusCode:" + statusCode);
                    process.errorReader().lines().forEach(System.out::println);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}