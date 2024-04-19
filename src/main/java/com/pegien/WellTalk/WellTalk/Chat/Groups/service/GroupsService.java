package com.pegien.WellTalk.WellTalk.Chat.Groups.service;

import com.pegien.WellTalk.WellTalk.Auth.AuthService;
import com.pegien.WellTalk.WellTalk.Chat.Groups.entity.Group;
import com.pegien.WellTalk.WellTalk.Chat.Groups.entity.GroupMemberShip;
import com.pegien.WellTalk.WellTalk.Chat.Groups.enums.GroupMembershipType;
import com.pegien.WellTalk.WellTalk.Chat.Groups.models.requests.AddMemberRequest;
import com.pegien.WellTalk.WellTalk.Chat.Groups.models.requests.AdminMemberRequest;
import com.pegien.WellTalk.WellTalk.Chat.Groups.models.requests.CreateGroupRequest;
import com.pegien.WellTalk.WellTalk.Chat.Groups.repository.GroupMemberShipRepository;
import com.pegien.WellTalk.WellTalk.Chat.Groups.repository.GroupRepository;
import com.pegien.WellTalk.WellTalk.Profile.entity.Profile;
import com.pegien.WellTalk.WellTalk.Profile.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class GroupsService {

    @Autowired
    private AuthService authService;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private GroupMemberShipRepository groupMemberShipRepository;

    public ResponseEntity<Group> createGroup(CreateGroupRequest createGroupRequest) {
        Group group=Group.builder()
                .creatorId(authService.getActiveProfile().getUid())
                .createdOn(Instant.now().toEpochMilli())
                .title(createGroupRequest.getTitle())
                .description(createGroupRequest.getDescription())
                .groupType(createGroupRequest.getGroupType())
                .members(1)
                .build();

        groupRepository.saveAndFlush(group);

        GroupMemberShip groupMemberShip = new GroupMemberShip();
        groupMemberShip.setGroupId(group.getUid());
        groupMemberShip.setProfileId(authService.getActiveProfile().getUid());
        groupMemberShip.setMembershipType(GroupMembershipType.CREATOR);
        groupMemberShip.setFirstAddedOn(Instant.now().toEpochMilli());
        groupMemberShip.setLastAddedOn(Instant.now().toEpochMilli());
        groupMemberShipRepository.save(groupMemberShip);


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


    public ResponseEntity<String> addMember(AddMemberRequest addMemberRequest) {
        Optional<Group> optionalGroup=groupRepository.findById(addMemberRequest.getGroupId());
        if(optionalGroup.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group Not Found");

        Optional<Profile> optionalProfile = profileRepository.findById(addMemberRequest.getProfileId());
        if(optionalProfile.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("profile Not Found");

        Group group=optionalGroup.get();
        Profile profile=optionalProfile.get();


        if(groupMembers(group.getUid()).contains(profile.getUid()))
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Member Already Exists");
        } else {

            Profile adder=authService.getActiveProfile();
            Optional<GroupMemberShip> optionalAdderMemberShip=groupMemberShipRepository.findByGroupIdAndProfileId(group.getUid(),adder.getUid());
            if(optionalAdderMemberShip.isEmpty()||optionalAdderMemberShip.get().getMembershipType()==GroupMembershipType.MEMBER)
                return ResponseEntity.status(HttpStatus.CONFLICT).body("You are not An Admin");

            GroupMemberShip groupMemberShip=null;
            Optional<GroupMemberShip> optionalGroupMemberShip=groupMemberShipRepository.findByGroupIdAndProfileId(group.getUid(),profile.getUid());
            if(optionalGroupMemberShip.isPresent())
            {
                groupMemberShip=optionalGroupMemberShip.get();
                groupMemberShip.setAddedBy(adder.getUid());
                groupMemberShip.setLastAddedOn(Instant.now().toEpochMilli());
                groupMemberShip.setMembershipType(GroupMembershipType.MEMBER);
                groupMemberShip.setRemovedOn(null);
            } else
            {
                groupMemberShip= new GroupMemberShip();
                groupMemberShip.setGroupId(group.getUid());
                groupMemberShip.setProfileId(profile.getUid());
                groupMemberShip.setMembershipType(GroupMembershipType.MEMBER);
                groupMemberShip.setFirstAddedOn(Instant.now().toEpochMilli());
                groupMemberShip.setLastAddedOn(Instant.now().toEpochMilli());
                groupMemberShip.setAddedBy(adder.getUid());
            }

            group.setMembers(group.getMembers()+1);

            groupMemberShipRepository.save(groupMemberShip);
            groupRepository.save(group);

            return ResponseEntity.ok("Added Successfully");
        }

    }




    public List<UUID> groupMembers(UUID group)
    {
        return groupMemberShipRepository.findGroupMembers(group);
    }

    public ResponseEntity<String> makeAdmin(AdminMemberRequest adminMemberRequest) {
        Optional<Group> optionalGroup=groupRepository.findById(adminMemberRequest.getGroupId());
        if(optionalGroup.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group Not Found");

        Optional<Profile> optionalProfile = profileRepository.findById(adminMemberRequest.getProfileId());
        if(optionalProfile.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("profile Not Found");

        Group group=optionalGroup.get();
        Profile profile=optionalProfile.get();

        Profile adder=authService.getActiveProfile();
        Optional<GroupMemberShip> optionalAdderMemberShip=groupMemberShipRepository.findByGroupIdAndProfileId(group.getUid(),adder.getUid());
        if(optionalAdderMemberShip.isEmpty()||optionalAdderMemberShip.get().getMembershipType()==GroupMembershipType.MEMBER)
            return ResponseEntity.status(HttpStatus.CONFLICT).body("You are not An Admin");



        if(groupMembers(group.getUid()).contains(profile.getUid()))
        {
            Optional<GroupMemberShip> optionalGroupMemberShip=groupMemberShipRepository.findByGroupIdAndProfileId(group.getUid(),profile.getUid());
            if(optionalGroupMemberShip.isPresent())
            {
                GroupMemberShip groupMemberShip=optionalGroupMemberShip.get();
                if(groupMemberShip.getMembershipType()==GroupMembershipType.MEMBER) {
                    groupMemberShip.setMembershipType(GroupMembershipType.ADMIN);
                    groupMemberShipRepository.save(groupMemberShip);
                    return ResponseEntity.ok("Admin Added Successfully");
                }
            }

        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Member Removed");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Un Expected Error Occured");
    }

    public ResponseEntity<String> removeAdmin(AdminMemberRequest adminMemberRequest) {
        Optional<Group> optionalGroup=groupRepository.findById(adminMemberRequest.getGroupId());
        if(optionalGroup.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group Not Found");

        Optional<Profile> optionalProfile = profileRepository.findById(adminMemberRequest.getProfileId());
        if(optionalProfile.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("profile Not Found");

        Group group=optionalGroup.get();
        Profile profile=optionalProfile.get();


        Profile adder=authService.getActiveProfile();
        Optional<GroupMemberShip> optionalAdderMemberShip=groupMemberShipRepository.findByGroupIdAndProfileId(group.getUid(),adder.getUid());
        if(optionalAdderMemberShip.isEmpty()||optionalAdderMemberShip.get().getMembershipType()==GroupMembershipType.MEMBER)
            return ResponseEntity.status(HttpStatus.CONFLICT).body("You are not An Admin");



        if(groupMembers(group.getUid()).contains(profile.getUid()))
        {
            Optional<GroupMemberShip> optionalGroupMemberShip=groupMemberShipRepository.findByGroupIdAndProfileId(group.getUid(),profile.getUid());
            if(optionalGroupMemberShip.isPresent())
            {
                GroupMemberShip groupMemberShip=optionalGroupMemberShip.get();
                if(groupMemberShip.getMembershipType()==GroupMembershipType.ADMIN) {
                    groupMemberShip.setMembershipType(GroupMembershipType.MEMBER);
                    groupMemberShipRepository.save(groupMemberShip);
                    return ResponseEntity.ok("Admin Removed Successfully");
                }
            }

        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Member Removed");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Un Expected Error Occured");
    }

    public ResponseEntity<String> removeMember(AddMemberRequest addMemberRequest) {
        Optional<Group> optionalGroup=groupRepository.findById(addMemberRequest.getGroupId());
        if(optionalGroup.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group Not Found");

        Optional<Profile> optionalProfile = profileRepository.findById(addMemberRequest.getProfileId());
        if(optionalProfile.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("profile Not Found");

        Group group=optionalGroup.get();
        Profile profile=optionalProfile.get();


        Profile adder=authService.getActiveProfile();
        Optional<GroupMemberShip> optionalAdderMemberShip=groupMemberShipRepository.findByGroupIdAndProfileId(group.getUid(),adder.getUid());
        if(optionalAdderMemberShip.isEmpty()||optionalAdderMemberShip.get().getMembershipType()==GroupMembershipType.MEMBER)
            return ResponseEntity.status(HttpStatus.CONFLICT).body("You are not An Admin");



        if(groupMembers(group.getUid()).contains(profile.getUid()))
        {
            Optional<GroupMemberShip> optionalGroupMemberShip=groupMemberShipRepository.findByGroupIdAndProfileId(group.getUid(),profile.getUid());
            if(optionalGroupMemberShip.isPresent())
            {
                GroupMemberShip groupMemberShip=optionalGroupMemberShip.get();
                if(groupMemberShip.getMembershipType()==GroupMembershipType.CREATOR)
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Cant remove group creator");
                else {
                    groupMemberShip.setRemovedOn(Instant.now().toEpochMilli());
                    groupMemberShip.setRemovedBy(adder.getUid());
                    group.setMembers(group.getMembers()-1);
                    groupRepository.save(group);
                    groupMemberShipRepository.save(groupMemberShip);
                    return ResponseEntity.ok("Member Removed Successfully");
                }

            }

        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Member Removed");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Un Expected Error Occured");
    }

    public ResponseEntity<List<Group>> listUserGroups() {
        return ResponseEntity.ok(userGroups());
    }

    public List<Group> userGroups()
    {
        Profile me=authService.getActiveProfile();
        List<Group> myGroups=new ArrayList<>();
        for(UUID groupId:groupMemberShipRepository.findGroupsByMember(me.getUid()))
            myGroups.add(groupRepository.findById(groupId).get());
        return myGroups;
    }

    public ResponseEntity<List<Profile>> listGroupMembers(UUID groupId) {
        List<UUID> membersId=groupMembers(groupId);
        Profile me=authService.getActiveProfile();
        if(!membersId.contains(me.getUid()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        List<Profile> users= new ArrayList<>();
        for(UUID profileId:membersId)
            users.add(profileRepository.findById(profileId).get());

        return ResponseEntity.ok(users);
    }
}
