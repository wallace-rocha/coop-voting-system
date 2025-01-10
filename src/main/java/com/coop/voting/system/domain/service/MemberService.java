package com.coop.voting.system.domain.service;

import com.coop.voting.system.domain.model.Member;
import com.coop.voting.system.domain.model.exception.MemberAlreadyRegisteredException;
import com.coop.voting.system.domain.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MemberService {

    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    private CpfValidationService cpfValidationService;

    @Transactional
    public Member createMember(String cpf, String name) throws Exception {
        logger.info("Starting the creation of member with CPF: {}", cpf);
        try {
            logger.info("Checking eligibility of CPF: {}", cpf);
            cpfValidationService.checkCpfEligibility(cpf);

            Optional<Member> memberExists = memberRepository.findByCpf(cpf);
            if (memberExists.isPresent()) {
                logger.warn("Attempt to register an already registered member with CPF: {}", cpf);
                throw new MemberAlreadyRegisteredException("Member already registered.");
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