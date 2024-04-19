package com.pegien.WellTalk.WellTalk.Chat.Groups.controller;

import com.pegien.WellTalk.WellTalk.Chat.Groups.entity.Group;
import com.pegien.WellTalk.WellTalk.Chat.Groups.models.requests.AddMemberRequest;
import com.pegien.WellTalk.WellTalk.Chat.Groups.models.requests.AdminMemberRequest;
import com.pegien.WellTalk.WellTalk.Chat.Groups.models.requests.CreateGroupRequest;
import com.pegien.WellTalk.WellTalk.Chat.Groups.service.GroupsService;
import com.pegien.WellTalk.WellTalk.Profile.entity.Profile;
import com.pegien.WellTalk.WellTalk.Utils.MyUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupsController {

    @Autowired
    private GroupsService groupsService;

    @PostMapping("/create")
    public ResponseEntity<Group> createGroup(@RequestBody @Valid CreateGroupRequest createGroupRequest, BindingResult result)
    {
        if(result.hasErrors())
            return MyUtils.createErrorResponse(result,Group.class);
        else
            return groupsService.createGroup(createGroupRequest);
    }

    @PutMapping("/update/{groupId}")
    public ResponseEntity<Group> updateGroup(@RequestBody @Valid CreateGroupRequest createGroupRequest,@PathVariable("groupId") String groupId, BindingResult result)
    {
        if(result.hasErrors())
            return MyUtils.createErrorResponse(result,Group.class);
        else
            return groupsService.updateGroup(groupId,createGroupRequest);
    }

    @PostMapping("/addMember")
    public ResponseEntity<String> addMember(@RequestBody @Valid AddMemberRequest addMemberRequest,BindingResult result) {
        if (result.hasErrors())
            return MyUtils.createErrorResponse(result, Group.class);
        else
            return groupsService.addMember(addMemberRequest);
    }

    @PostMapping("/makeAdmin")
    public ResponseEntity<String> makeAdmin(@RequestBody @Valid AdminMemberRequest adminMemberRequest, BindingResult result) {
        if (result.hasErrors())
            return MyUtils.createErrorResponse(result, Group.class);
        else
            return groupsService.makeAdmin(adminMemberRequest);
    }

    @PostMapping("/removeAdmin")
    public ResponseEntity<String> removeAdmin(@RequestBody @Valid AdminMemberRequest adminMemberRequest, BindingResult result) {
        if (result.hasErrors())
            return MyUtils.createErrorResponse(result, Group.class);
        else
            return groupsService.removeAdmin(adminMemberRequest);
    }

    @DeleteMapping("/removeMember")
    public ResponseEntity<String> removeMember(@RequestBody @Valid AddMemberRequest addMemberRequest, BindingResult result) {
        if (result.hasErrors())
            return MyUtils.createErrorResponse(result, Group.class);
        else
            return groupsService.removeMember(addMemberRequest);
    }


    @GetMapping("/listGroups")
    public ResponseEntity<List<Group>> listUserGroups()
    {
        return groupsService.listUserGroups();
    }

    @GetMapping("/listMembers/{groupId}")
    public ResponseEntity<List<Profile>> groupMembers(@PathVariable("groupId")UUID groupId)
    {
        return groupsService.listGroupMembers(groupId);
    }


}
