package org.app.gtsconnected3.controller.client;

import lombok.extern.slf4j.Slf4j;
import org.app.gtsconnected3.dto.ChatMessage;
import org.app.gtsconnected3.entity.Message;
import org.app.gtsconnected3.entity.User;
import org.app.gtsconnected3.service.MessageService;
import org.app.gtsconnected3.service.TripService;
import org.app.gtsconnected3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
@Slf4j
public class WebSocketChatController {

    private final MessageService messageService;
    private final UserService userService;
    private final TripService tripService;

    @Autowired
    public WebSocketChatController(MessageService messageService, UserService userService, TripService tripService) {
        this.messageService = messageService;
        this.userService = userService;
        this.tripService = tripService;
    }

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        Long senderId = Long.parseLong(HtmlUtils.htmlEscape(message.getSenderId()));
        Long receiverId = Long.parseLong(HtmlUtils.htmlEscape(message.getReceiverId()));
        Long tripId = Long.parseLong(HtmlUtils.htmlEscape(message.getTripId()));
        String msg = message.getContent();

        log.info("Sender: {} Receiver: {} Trip: {} Message: {}", senderId, receiverId, tripId, msg);

        User sender = userService.findUserById(senderId);

        Message toSaveMsg = new Message(userService.findUserById(senderId), userService.findUserById(receiverId), tripService.findById(tripId) ,msg);
        messageService.save(toSaveMsg);

        return new ChatMessage(
                HtmlUtils.htmlEscape(message.getContent()),
                senderId.toString(),
                sender.getFirstName() + " " + sender.getLastName(),
                receiverId.toString(),
                tripId.toString()
        );
    }
}

