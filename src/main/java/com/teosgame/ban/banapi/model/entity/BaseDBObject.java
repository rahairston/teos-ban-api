package com.teosgame.ban.banapi.model.entity;

import java.util.Date;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

public abstract class BaseDBObject {
    @CreatedBy
    String createdBy;
    
    @LastModifiedBy
    String modifiedBy;
    
    @CreatedDate
    Date createdAt;
    
    @LastModifiedDate
    Date modifiedAt;
}
