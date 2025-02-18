package com.pegien.WellTalk.WellTalk.Chat.Groups.repository;

import com.pegien.WellTalk.WellTalk.Chat.Groups.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GroupRepository extends JpaRepository<Group, UUID> {


}
