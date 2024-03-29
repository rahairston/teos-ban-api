package com.teosgame.ban.banapi.persistence;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teosgame.ban.banapi.model.entity.AppealEntity;

@Repository
public interface BanAppealRepository extends PagingAndSortingRepository<AppealEntity, String> {
    public List<AppealEntity> findByTwitchUsernameIgnoreCase(String twitchUsername);
    @Query(value = "select * from appeals a LEFT JOIN judgement j on a.JUDGEMENT_ID = j.JUDGEMENT_ID " +
            "where (:status is null or j.STATUS=:status) and (:type is null or a.BAN_TYPE=:type) and " +
            "(:username is null or a.TWITCH_USERNAME=:username) ORDER BY a.CREATED_AT", nativeQuery = true,
            countQuery = "select COUNT(a.APPEAL_ID) from appeals a LEFT JOIN judgement j on a.JUDGEMENT_ID = j.JUDGEMENT_ID " +
            "where (:status is null or j.STATUS=:status) and (:type is null or a.BAN_TYPE=:type) and " +
            "(:username is null or a.TWITCH_USERNAME=:username)")
    public Page<AppealEntity> findByUsernameAndBanTypeAndJudgmentStatus(
        @Param("username") String username,
        @Param("type") String type,
        @Param("status") String status,
        Pageable pageable);

    @Query(value = "select COUNT(a.APPEAL_ID) from appeals a LEFT JOIN judgement j on a.JUDGEMENT_ID = j.JUDGEMENT_ID " +
            "where a.TWITCH_USERNAME=:username AND j.STATUS in ('PENDING', 'REVIEWING')", nativeQuery = true)
    public int countPendingByUsername(@Param("username") String username);

    // For getting the next and previous pages
    @Query(value = "select APPEAL_ID from appeals " +
            "where CREATED_AT < :date LIMIT 1", nativeQuery = true)
    public Optional<String> findAppealIdBefore(Date date);
    @Query(value = "select APPEAL_ID from appeals " +
            "where CREATED_AT > :date LIMIT 1", nativeQuery = true)
    public Optional<String> findAppealIdAfter(Date date);
}
