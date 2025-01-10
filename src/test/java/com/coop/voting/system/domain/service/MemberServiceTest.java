package com.coop.voting.system.domain.service;

import com.coop.voting.system.domain.model.Member;
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
        Member existingMember = new Member();
        existingMember.setCpf(existingCpf);

        when(memberRepository.findByCpf(existingCpf)).thenReturn(Optional.of(existingMember));

        MemberAlreadyRegisteredException exception = assertThrows(MemberAlreadyRegisteredException.class, () -> {
            memberService.createMember(existingCpf, memberName);
        });

        assertEquals("Member already registered.", exception.getMessage());
    }

}