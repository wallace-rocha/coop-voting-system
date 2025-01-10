package com.coop.voting.system.api.controller;

import com.coop.voting.system.domain.model.Member;
import com.coop.voting.system.domain.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
public class MemberController {

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private MemberService memberService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Member createMember(@RequestParam String cpf, @RequestParam String name) throws Exception {
        logger.info("Received request to create member with CPF: {}", cpf);

        try {
            Member member = memberService.createMember(cpf, name);
            logger.info("Member created successfully: {}", member);
            return member;
        } catch (Exception e) {
            logger.error("Error creating member with CPF: {}", cpf, e);
            throw e;
        }
    }
}