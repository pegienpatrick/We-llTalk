package com.pegien.WellTalk.WellTalk.Chat.Groups.repository;

import com.pegien.WellTalk.WellTalk.Chat.Groups.entity.GroupMemberShip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GroupMemberShipRepository extends JpaRepository<GroupMemberShip, UUID> {
}
