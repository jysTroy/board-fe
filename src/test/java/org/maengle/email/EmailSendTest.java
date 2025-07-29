package org.maengle.email;

import org.junit.jupiter.api.Test;
import org.maengle.global.email.entities.EmailMessage;
import org.maengle.global.email.services.EmailSendService;
import org.maengle.global.email.services.EmailVerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class EmailSendTest {
    @Autowired
    private EmailSendService emailSendService;

    @Autowired
    private EmailVerifyService emailVerifyService;

    @Test
    void sendTest() {
//        EmailMessage message = new EmailMessage("tailred215@gmail.com", "메일보내기 테스트", "성공...");
//        boolean success = emailSendService.sendEmail(message);
//
//        assertTrue(success);
        EmailMessage message = new EmailMessage("tailred215@gmail.com", "메일보내기 테스트...", "성공...");
        Map<String,Object> tplData = new HashMap<>();
        tplData.put("authNum", "123456");
        System.out.println(tplData.get("authNum"));

        boolean success = emailSendService.sendEmail(message, "auth", tplData);

        assertTrue(success);
    }

    @Test
    void test2(){
        boolean result = emailVerifyService.sendCode("tailred215@gmail.com");
        assertTrue(result);
    }

    @Test
    void test3(){

    }
}
