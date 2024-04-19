package com.pegien.WellTalk.WellTalk.Chat.ProfileStatus.repository;

import com.pegien.WellTalk.WellTalk.Chat.ProfileStatus.entity.ProfileStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfileStatusRepository extends JpaRepository<ProfileStatus, UUID> {

    @Query("select p from ProfileStatus p where p.subjectId=?1 and p.typingPartner=?2")
    Optional<ProfileStatus> findProfileStatus(UUID subjectId, UUID partnerId);
}
