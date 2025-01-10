package com.coop.voting.system.domain.service;

import com.coop.voting.system.domain.model.Member;
import com.coop.voting.system.domain.model.exception.CpfInvalidException;
import com.coop.voting.system.domain.model.exception.MemberAlreadyRegisteredException;
import com.coop.voting.system.domain.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CpfValidationService cpfValidationService;

    @InjectMocks
    private MemberService memberService;

    private final String validCpf = "12345678900";
    private final String existingCpf = "09876543210";
    private final String memberName = "Wallace Rocha";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateMember_Success() throws Exception {
        doNothing().when(cpfValidationService).checkCpfEligibility(validCpf);
        when(memberRepository.findByCpf(validCpf)).thenReturn(Optional.empty());

        Member member = Member.builder()
                .cpf(validCpf)
                .name(memberName)
                .inclusionDate(LocalDateTime.now())
                .build();

        when(memberRepository.save(any(Member.class))).thenReturn(member);

        Member result = memberService.createMember(validCpf, memberName);

        assertNotNull(result);
        assertEquals(validCpf, result.getCpf());
        assertEquals(memberName, result.getName());
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    void testCreateMember_ThrowsMemberAlreadyRegisteredException() throws Exception {
        doNothing().when(cpfValidationService).checkCpfEligibility(validCpf);
        Member existingMember = new Member();
        existingMember.setCpf(existingCpf);

        when(memberRepository.findByCpf(existingCpf)).thenReturn(Optional.of(existingMember));

        MemberAlreadyRegisteredException exception = assertThrows(MemberAlreadyRegisteredException.class, () -> {
            memberService.createMember(existingCpf, memberName);
        });

        assertEquals("Member already registered.", exception.getMessage());
    }

    @Test
    void testCreateMember_CpfInvalid() throws Exception {
        doThrow(new CpfInvalidException("The provided CPF is invalid.")).when(cpfValidationService).checkCpfEligibility(validCpf);

        CpfInvalidException exception = assertThrows(CpfInvalidException.class, () -> {
            memberService.createMember(validCpf, memberName);
        });

        assertEquals("The provided CPF is invalid.", exception.getMessage());
    }

}