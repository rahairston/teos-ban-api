package com.teosgame.ban.banapi.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.teosgame.ban.banapi.model.entity.AppealEntity;

@Repository
public interface BanAppealRepository extends CrudRepository<AppealEntity, String> {
    
}
