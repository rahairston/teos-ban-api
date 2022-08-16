package com.teosgame.ban.banapi.model.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BANNED_BY")
public class BannedByEntity {
    @Id
    @Column(name = "BANNED_BY_ID", unique = true, length=36)
    private final String id = UUID.randomUUID().toString();

    @Column(nullable = false, columnDefinition = "TINYTEXT")
    String name;

    // String due to potential "pre-YEAR" since some bans were prior to tracking stats
    @Column(nullable = false, length=20)
    String banDate;
}
