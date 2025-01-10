package com.coop.voting.system.domain.repository;

import com.coop.voting.system.domain.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {

    Optional<Member> findByCpf(String cpf);
}