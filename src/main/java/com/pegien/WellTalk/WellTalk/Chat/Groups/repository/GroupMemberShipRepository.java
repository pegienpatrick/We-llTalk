package com.pegien.WellTalk.WellTalk.Chat.Groups.repository;

import com.pegien.WellTalk.WellTalk.Chat.Groups.entity.GroupMemberShip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupMemberShipRepository extends JpaRepository<GroupMemberShip, UUID> {

    @Query("select m.profileId from GroupMemberShip m where m.removedOn is null or m.removedOn<=0")
    List<UUID> findGroupMembers(UUID group);

    Optional<GroupMemberShip> findByGroupIdAndProfileId(UUID groupId, UUID profileId);

    @Query("select groupId from GroupMemberShip m where m.removedOn is null or m.removedOn<=0")
    List<UUID> findGroupsByMember(UUID profileId);
}
