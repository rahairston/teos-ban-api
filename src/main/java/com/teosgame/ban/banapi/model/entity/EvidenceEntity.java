package com.teosgame.ban.banapi.model.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "EVIDENCE")
public class EvidenceEntity extends BaseDBObject {
    @Id
    @Column(name = "EVIDENCE_ID", unique = true, length=36)
    private final String id = UUID.randomUUID().toString();

    @JoinColumn(name="appeal_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    AppealEntity appeal;

    @Column(nullable = true, columnDefinition = "TINYTEXT")
    String notes;

    // I don't think I'll need any File path stored due to the potential
    // for deriving it from existing variables:
    // [host]://[SomeFileBase]/appeal/:appealId/evidence/evidenceId.jpg or whatever
    // due to S3 presigned urls only allowing
}
