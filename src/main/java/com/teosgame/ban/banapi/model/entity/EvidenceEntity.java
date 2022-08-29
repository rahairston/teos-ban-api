package com.teosgame.ban.banapi.model.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "EVIDENCE")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class EvidenceEntity extends BaseDBObject {
    @Id
    @Column(name = "EVIDENCE_ID", unique = true, length=36)
    @EqualsAndHashCode.Include
    private final String id = UUID.randomUUID().toString();

    @JoinColumn(name="appeal_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    AppealEntity appeal;

    @Column(nullable = false, length = 6)
    String fileExtension; // TODO enum with acceptable as .png .jpg .jpeg?

    @Column(nullable = true, columnDefinition = "TINYTEXT")
    String notes;

    // I don't think I'll need any File path stored due to the potential
    // for deriving it from existing variables:
    // [host]://[SomeFileBase]/appeal/:appealId/evidence/evidenceId.jpg or whatever
    // due to S3 presigned urls only allowing
}
