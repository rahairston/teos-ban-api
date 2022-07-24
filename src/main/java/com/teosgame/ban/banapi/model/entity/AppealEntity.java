package com.teosgame.ban.banapi.model.entity;

import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.teosgame.ban.banapi.model.enums.BanType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "APPEALS")
public class AppealEntity extends BaseDBObject {
    @Id
    @Column(name = "APPEAL_ID", unique = true, length=36)
    private final String id = UUID.randomUUID().toString();

    @Column(nullable = false, length=25)
    String twitchUsername;

    @Column(nullable = true, length=32)
    String discordUsername;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    BanType banType;

    @Column(nullable = false, columnDefinition = "TINYTEXT")
    String banReason;

    @Column(nullable = false)
    Boolean banJustified;

    @Column(nullable = false, columnDefinition = "TEXT")
    String appealReason;

    @Column(nullable = true, columnDefinition = "TINYTEXT")
    String additionalNotes;

    ////////////////// RESUBMISSION VARIABLES //////////////////

    @OneToOne(fetch = FetchType.LAZY)
    AppealEntity previous;

    @Column(nullable = true)
    String additionalData;

    ////////////////// ADMIN VARIABLES //////////////////

    @Column(nullable = true)
    @OneToMany(mappedBy="appeal", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    List<EvidenceEntity> evidence;

    @OneToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name = "JUDGEMENT_ID")
    JudgementEntity judgement;
}
