package com.coop.voting.system.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Agenda {

    @Id
    @GeneratedValue
    private UUID agendaId;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false, updatable = false)
    private LocalDateTime inclusionDate;

    private LocalDateTime votingStartDate;

    private LocalDateTime votingEndDate;
}