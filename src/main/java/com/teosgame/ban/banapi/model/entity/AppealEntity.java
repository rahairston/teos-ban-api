package com.teosgame.ban.banapi.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.teosgame.ban.banapi.model.enums.BanType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
public class AppealEntity extends BaseDBObject {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private String id;

    String twitchUsername;
    String discordUsername;
    BanType banType;
    String banStatus;
    String banReason;
    Boolean banJustified;
    String appealReason;
    String additionalNotes;
    String previousAppealId;
    String additionalData;
}
