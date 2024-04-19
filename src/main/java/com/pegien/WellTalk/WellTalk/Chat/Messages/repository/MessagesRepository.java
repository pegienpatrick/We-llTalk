package com.pegien.WellTalk.WellTalk.Chat.Messages.repository;

import com.pegien.WellTalk.WellTalk.Chat.Messages.entity.Message;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessagesRepository extends JpaRepository<Message, UUID> {

    @Query("select messageSourceUid as partnerId from Message m where destinationUid=?1 union select destinationUid as partnerId from Message m where messageSourceUid=?1 group by partnerId order by max(createDate) desc")
    List<UUID> findPartners(UUID sender);

    @Query("select count(*) from Message where destinationUid=?1 and messageSourceUid=?2 and (readDate is null or readDate<=0)")
    int chatsCounter(UUID userId,UUID partnerId);

    @Query(value = "select m.* from Message m where (destinationUid=?1 and messageSourceUid=?2) or (destinationUid=?2 and messageSourceUid=?1) order by createDate desc limit 1",nativeQuery = true)
    Message lastMessage(UUID uid, UUID partnerId);

    @Query("select m from Message m where (destinationUid=?1 and messageSourceUid=?2) or (destinationUid=?2 and messageSourceUid=?1) order by createDate desc")
    List<Message> findMessagesWith(UUID profileId,UUID partnerId);


    @Transactional
    @Query("update Message set deliveredDate=?1 where destinationUid=?2 and (deliveredDate is null or deliveredDate<=0)")
    void deliverMessages(Long date,UUID profileId);

    @Transactional
    @Query("update Message set readDate=?1 where destinationUid=?2 and (readDate is null or readDate<=0)")
    void readMessages(Long date,UUID profileId);


}
