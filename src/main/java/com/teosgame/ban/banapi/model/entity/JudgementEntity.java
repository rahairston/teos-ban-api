package com.teosgame.ban.banapi.model.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity
@Table(name = "JUDGEMENT")
public class JudgementEntity {
    @Id
    @Column(name = "JUDGEMENT_ID", unique = true, length=36)
    private final String id = UUID.randomUUID().toString();

    @JoinColumn(name="APPEAL_ID", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    AppealEntity appeal;

    @Column(name="STATUS", nullable = false, length=21) 
    String status;

    @Column(name="NOTES", columnDefinition = "TINYTEXT", nullable = true)
    String notes;
}
