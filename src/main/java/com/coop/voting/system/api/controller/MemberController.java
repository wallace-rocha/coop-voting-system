package com.coop.voting.system.api.controller;

import com.coop.voting.system.domain.model.Member;
import com.coop.voting.system.domain.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Member createMember(@RequestParam String cpf, @RequestParam String name) throws Exception {

        try {
            Member member = memberService.createMember(cpf, name);
            return member;
        } catch (Exception e) {
            throw e;
        }
    }
}