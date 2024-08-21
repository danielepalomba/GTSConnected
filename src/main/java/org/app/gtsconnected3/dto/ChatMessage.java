package org.app.gtsconnected3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatMessage {
    private String content;
    private String senderId;
    private String senderName;
    private String receiverId;
    private String tripId;
}
