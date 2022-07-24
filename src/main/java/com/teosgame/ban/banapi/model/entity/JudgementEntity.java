package com.teosgame.ban.banapi.model.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.teosgame.ban.banapi.model.enums.JudgementStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "JUDGEMENT")
public class JudgementEntity extends BaseDBObject {
    @Id
    @Column(name = "JUDGEMENT_ID", unique = true, length=36)
    private final String id = UUID.randomUUID().toString();

    @JoinColumn(name="APPEAL_ID", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    AppealEntity appeal;

    @Column(name="STATUS", nullable = false, length=21)
    @Enumerated(value = EnumType.STRING)
    JudgementStatus status;

    @Column(name="RESUBMITTABLE", nullable = true)
    Boolean resubmittable; //use object Boolean so we can null it

    @Column(name="RESUBMIT_DT", nullable = true)
    Date resubmitAfterDate;

    @Column(name="NOTES", columnDefinition = "TINYTEXT", nullable = true)
    String notes;

    public boolean isResubmittable() {
        return resubmittable.booleanValue();
    }
}
