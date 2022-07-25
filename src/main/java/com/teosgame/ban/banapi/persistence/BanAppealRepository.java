package com.teosgame.ban.banapi.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teosgame.ban.banapi.model.entity.AppealEntity;

@Repository
public interface BanAppealRepository extends CrudRepository<AppealEntity, String> {
    public List<AppealEntity> findByTwitchUsername(String twitchUsername);

    @Query(value = "select COUNT(a.APPEAL_ID) from APPEALS a LEFT JOIN JUDGEMENT j on a.JUDGEMENT_ID = j.JUDGEMENT_ID " +
            "where a.TWITCH_USERNAME=:username AND j.STATUS in ('PENDING', 'REVIEWING')", nativeQuery = true)
    public int countPendingByUsername(@Param("username") String username);
}
