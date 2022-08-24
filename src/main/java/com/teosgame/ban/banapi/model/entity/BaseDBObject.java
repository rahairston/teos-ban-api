package com.teosgame.ban.banapi.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@SuperBuilder
@NoArgsConstructor
@Setter
@Getter
public abstract class BaseDBObject {
    @CreatedBy
    @Column(nullable = false)
    String createdBy;
    
    @LastModifiedBy
    @Column(nullable = true)
    String modifiedBy;
    
    @CreatedDate
    @Column(nullable = false)
    Date createdAt = new Date();
    
    @LastModifiedDate
    @Column(nullable = true)
    Date modifiedAt;

    @Version
    Long version;
}
