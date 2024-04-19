package com.pegien.WellTalk.WellTalk.Chat.Messages.repository;

import com.pegien.WellTalk.WellTalk.Chat.Messages.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MessagesRepository extends JpaRepository<Message, UUID> {
}
