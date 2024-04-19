package com.pegien.WellTalk.WellTalk.Chat.Groups.controller;

import com.pegien.WellTalk.WellTalk.Chat.Groups.entity.Group;
import com.pegien.WellTalk.WellTalk.Chat.Groups.models.requests.CreateGroupRequest;
import com.pegien.WellTalk.WellTalk.Chat.Groups.service.GroupsService;
import com.pegien.WellTalk.WellTalk.Utils.MyUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

}
