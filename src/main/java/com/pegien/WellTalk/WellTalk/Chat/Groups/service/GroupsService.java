package com.pegien.WellTalk.WellTalk.Chat.Groups.service;

import com.pegien.WellTalk.WellTalk.Auth.AuthService;
import com.pegien.WellTalk.WellTalk.Chat.Groups.entity.Group;
import com.pegien.WellTalk.WellTalk.Chat.Groups.models.requests.CreateGroupRequest;
import com.pegien.WellTalk.WellTalk.Chat.Groups.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class GroupsService {

    @Autowired
    private AuthService authService;

    @Autowired
    private GroupRepository groupRepository;

    public ResponseEntity<Group> createGroup(CreateGroupRequest createGroupRequest) {
        Group group=Group.builder()
                .creatorId(authService.getActiveProfile().getUid())
                .createdOn(Instant.now().toEpochMilli())
                .title(createGroupRequest.getTitle())
                .description(createGroupRequest.getDescription())
                .groupType(createGroupRequest.getGroupType())
                .build();

        groupRepository.saveAndFlush(group);
        return ResponseEntity.status(HttpStatus.CREATED).body(group);
    }


    public ResponseEntity<Group> updateGroup(String groupId, CreateGroupRequest createGroupRequest) {
        Optional<Group> optionalGroup=groupRepository.findById(UUID.fromString(groupId));
        if(optionalGroup.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        Group group=optionalGroup.get();

        group.setGroupType(createGroupRequest.getGroupType());
        group.setDescription(createGroupRequest.getDescription());
        group.setTitle(createGroupRequest.getTitle());


        groupRepository.saveAndFlush(group);

        return ResponseEntity.ok(group);
    }
}
