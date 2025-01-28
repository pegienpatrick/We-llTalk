package com.pegien.WellTalk.WellTalk.Chat.Messages.Service;

import com.pegien.WellTalk.WellTalk.Auth.AuthService;
import com.pegien.WellTalk.WellTalk.Chat.Groups.entity.Group;
import com.pegien.WellTalk.WellTalk.Chat.Groups.service.GroupsService;
import com.pegien.WellTalk.WellTalk.Chat.Messages.entity.Message;
import com.pegien.WellTalk.WellTalk.Chat.Messages.entity.TextMessage;
import com.pegien.WellTalk.WellTalk.Chat.Messages.enums.MessageDestinationType;
import com.pegien.WellTalk.WellTalk.Chat.Messages.model.requests.TextMessageRequest;
import com.pegien.WellTalk.WellTalk.Chat.Messages.model.responses.ChatListResponse;
import com.pegien.WellTalk.WellTalk.Chat.Messages.repository.MessagesRepository;
import com.pegien.WellTalk.WellTalk.Chat.ProfileStatus.entity.ProfileStatus;
import com.pegien.WellTalk.WellTalk.Chat.ProfileStatus.repository.ProfileStatusRepository;
import com.pegien.WellTalk.WellTalk.Chat.ProfileStatus.utils.ProfileStatusUtils;
import com.pegien.WellTalk.WellTalk.Configurations.GenConfigs;
import com.pegien.WellTalk.WellTalk.Profile.entity.Profile;
import com.pegien.WellTalk.WellTalk.Profile.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class MessagesService {

    @Autowired
    private AuthService authService;

    @Autowired
    private MessagesRepository messagesRepository;

    @Autowired
    private GroupsService groupsService;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileStatusRepository profileStatusRepository;

    public ResponseEntity<TextMessage> sendTextMessage(TextMessageRequest textMessageRequest) {
        Profile sender=authService.getActiveProfile();

        TextMessage to_send=new TextMessage();
        to_send.setMessageContent(textMessageRequest.getContent());
        to_send.setCreateDate(Instant.now().getEpochSecond());
        to_send.setDestinationType(textMessageRequest.getDestinationType());
        to_send.setDestinationUid(textMessageRequest.getRecipient());
        to_send.setMessageSourceUid(sender.getUid());

        messagesRepository.saveAndFlush(to_send);
        return ResponseEntity.status(HttpStatus.CREATED).body(to_send);
    }

    public List<ChatListResponse> listChats() {
        Profile me=authService.getActiveProfile();
        messagesRepository.deliverMessages(Instant.now().getEpochSecond(),me.getUid());
        List<Group> myGroups= groupsService.userGroups();
        HashMap<UUID,Group> groupsMap=new HashMap<>();
        for(Group group:myGroups)
            groupsMap.put(group.getUid(),group);

        List<ChatListResponse> chatLists=new ArrayList<>();

        List<UUID> partnerIds=messagesRepository.findPartners(me.getUid());
        for(UUID newChats:groupsMap.keySet())
            if(!partnerIds.contains(newChats))
                partnerIds.add(newChats);
        for(UUID partnerId:partnerIds)
        {
            ChatListResponse chatListResponse=new ChatListResponse();
            chatListResponse.setCounter(messagesRepository.chatsCounter(me.getUid(),partnerId));
            chatListResponse.setPartnerId(partnerId);
            chatListResponse.setPartnerType(groupsMap.containsKey(partnerId)? MessageDestinationType.GROUP:MessageDestinationType.INDIVIDUAL);
            Message lastMessage=messagesRepository.lastMessage(me.getUid(),partnerId);
            if(lastMessage!=null) {
                chatListResponse.setLastMessage(lastMessage);
                chatListResponse.setLastConv(lastMessage.getCreateDate());
            }
            if(groupsMap.containsKey(partnerId))
            {
                Group cGroup=groupsMap.get(partnerId);
                chatListResponse.setProfileName(cGroup.getMembers()+" Members");
                chatListResponse.setUsername(cGroup.getTitle());

            }
            else
            {
                Optional<Profile> optionalProfile=profileRepository.findById(partnerId);
                if(optionalProfile.isPresent())
                {
                    Profile partnerProfile=optionalProfile.get();
                    chatListResponse.setUsername(partnerProfile.getUsername());
                    chatListResponse.setProfileName(partnerProfile.getProfileName());

                    Long lastSeen = authService.lastSeen(partnerId);
                    chatListResponse.setLastSeen(lastSeen);

                    String status="offline";
                    Long rightNow=Instant.now().getEpochSecond();
                    Long dateNow=new Date().getTime();
                    if(rightNow-lastTyping(partnerId,me.getUid())< GenConfigs.typingThreshold)
                    {
                        status="typing ... ";
                    } else if (dateNow-authService.lastSeen(partnerId) < GenConfigs.onlineThreshold) {
                        status="online";
                    } else
                    {
                        Long diff=dateNow-authService.lastSeen(partnerId);
                        String tmpStat= ProfileStatusUtils.millisToComment(diff);
                        if(tmpStat.length()>1)
                            status = "last seen "+tmpStat;
                        else
                            status = "date";

                        chatListResponse.setLastSeen(lastSeen+(rightNow-dateNow));
                    }
                    chatListResponse.setStatus(status);

                }
            }
            chatLists.add(chatListResponse);
        }
        return chatLists;
    }

    public ResponseEntity<String> iamTyping(UUID partnerId) {
        Profile me=authService.getActiveProfile();
        ProfileStatus profileStatus;
        Optional<ProfileStatus> optionalProfileStatus=profileStatusRepository.findProfileStatus(me.getUid(),partnerId);
        if(optionalProfileStatus.isPresent())
           profileStatus=optionalProfileStatus.get();
        else
            profileStatus=new ProfileStatus();

        profileStatus.setTypingPartner(partnerId);
        profileStatus.setSubjectId(me.getUid());
        profileStatus.setLastTyping(Instant.now().getEpochSecond());

        profileStatusRepository.save(profileStatus);

        return ResponseEntity.ok("Sent Successfully");
    }


    public Long lastTyping(UUID subjectId,UUID partnerId)
    {
        Profile me=authService.getActiveProfile();
        ProfileStatus profileStatus;
        Optional<ProfileStatus> optionalProfileStatus=profileStatusRepository.findProfileStatus(me.getUid(),partnerId);
        if(optionalProfileStatus.isPresent()) {
            profileStatus = optionalProfileStatus.get();
            return profileStatus.getLastTyping();
        }
        return 0L;
    }

    public ResponseEntity<List<Message>> listMessages(UUID partnerId) {
        return ResponseEntity.ok(listMessage(partnerId));
    }

    private List<Message> listMessage(UUID partnerId) {
        Profile me=authService.getActiveProfile();
        messagesRepository.readMessages(Instant.now().toEpochMilli(),me.getUid());
        return messagesRepository.findMessagesWith(me.getUid(),partnerId);
    }
}
