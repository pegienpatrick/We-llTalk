package com.pegien.WellTalk.WellTalk.Chat.Messages.controller;


import com.pegien.WellTalk.WellTalk.Chat.Groups.entity.Group;
import com.pegien.WellTalk.WellTalk.Chat.Messages.Service.MessagesService;
import com.pegien.WellTalk.WellTalk.Chat.Messages.entity.Message;
import com.pegien.WellTalk.WellTalk.Chat.Messages.entity.TextMessage;
import com.pegien.WellTalk.WellTalk.Chat.Messages.model.requests.TextMessageRequest;
import com.pegien.WellTalk.WellTalk.Chat.Messages.model.responses.ChatListResponse;
import com.pegien.WellTalk.WellTalk.Utils.MyUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/message")
public class MessagesController {

    @Autowired
    private MessagesService messagesService;


    @PostMapping("/textMessage")
    public ResponseEntity<TextMessage> sendTextMessage(@RequestBody @Valid TextMessageRequest textMessageRequest, BindingResult result)
    {
        if(result.hasErrors())
            return MyUtils.createErrorResponse(result, Group.class);
        else
            return messagesService.sendTextMessage(textMessageRequest);
    }

    @GetMapping("/listChats")
    public ResponseEntity<List<ChatListResponse>> listChats()
    {
        return ResponseEntity.ok(messagesService.listChats());
    }

    @GetMapping("/typing/{partnerId}")
    public ResponseEntity<String> amTyping(@PathVariable("partnerId")UUID partnerId)
    {
        return messagesService.iamTyping(partnerId);
    }

    @GetMapping("/listMessages/{partnerId}")
    public ResponseEntity<List<Message>> listMessages(@PathVariable("partnerId") UUID partnerId)
    {
        return messagesService.listMessages(partnerId);
    }






}
