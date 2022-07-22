package com.teosgame.ban.banapi.model.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.teosgame.ban.banapi.model.enums.BanType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
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

    @Column(nullable = false, length=21)
    String banStatus;

    @Column(nullable = false, columnDefinition = "TINYTEXT")
    String banReason;

    @Column(nullable = false)
    Boolean banJustified;

    @Column(nullable = false, columnDefinition = "TEXT")
    String appealReason;

    @Column(nullable = true, columnDefinition = "TINYTEXT")
    String additionalNotes;

    @OneToOne(fetch = FetchType.LAZY)
    AppealEntity previous;

    @Column(nullable = true)
    String additionalData;
}
