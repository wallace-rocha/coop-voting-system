package com.coop.voting.system.domain.service;

import com.coop.voting.system.domain.model.Member;
import com.coop.voting.system.domain.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    MemberRepository memberRepository;

    @Transactional
    public Member createMember(String cpf, String name) throws Exception {
        try {
            Optional<Member> memberExists = memberRepository.findByCpf(cpf);
            if (memberExists.isPresent()) {
                throw new Exception("Member already registered.");
            }

            Member member = Member.builder()
                    .cpf(cpf)
                    .name(name)
                    .inclusionDate(LocalDateTime.now())
                    .build();

            return memberRepository.save(member);
        } catch (Exception e) {
            throw e;
        }
    }
}