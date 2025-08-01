package org.maengle.member.services;

import org.junit.jupiter.api.Test;
import org.maengle.member.controllers.RequestFindId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FindIdServiceTest {

    @Autowired
    private FindIdService service;

    @Test
    public void test1(){

        RequestFindId form = new RequestFindId("tailred215@gmail.com");

        service.process(form, null);
    }
}
