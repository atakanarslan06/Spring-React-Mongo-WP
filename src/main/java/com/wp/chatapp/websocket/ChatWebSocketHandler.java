package com.wp.chatapp.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wp.chatapp.business.dto.GroupMessageDto;
import com.wp.chatapp.business.dto.MessageDto;
import com.wp.chatapp.business.services.GroupMessageService;
import com.wp.chatapp.business.services.MessageService;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {
    private final MessageService messageService;
    private final GroupMessageService groupMessageService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ChatWebSocketHandler(MessageService messageService, GroupMessageService groupMessageService) {
        this.messageService = messageService;
        this.groupMessageService = groupMessageService;
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) {
        String payload = message.getPayload();
        try {
            // Gelen JSON mesajını uygun DTO nesnesine dönüştürme
            MessageDto messageDto = objectMapper.readValue(payload, MessageDto.class);
            // Gelen mesajın türüne göre işlem yapma
            if (messageDto.isGroupMessage()) {
                handleGroupMessage(messageDto);
            } else {
                handleMessage(messageDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // JSON dönüşüm hatası veya işlenemeyen bir hata durumunda buraya düşer
        }
    }

    private void handleMessage(MessageDto messageDto) {
        // Gelen bireysel mesajı işleme
        messageService.sendMessage(messageDto);
        // İşleme aldıktan sonra isteğe bağlı olarak diğer işlemler de yapılabilir
    }

    private void handleGroupMessage(MessageDto messageDto) {
        // Gelen mesajı GroupMessageDto'ya dönüştürme
        GroupMessageDto groupMessageDto = GroupMessageDto.builder()
                .groupId(messageDto.getGroupId())
                .senderId(messageDto.getSenderId())
                .content(messageDto.getContent())
                .timestamp(messageDto.getTimestamp())
                .build();

        // Oluşturulan GroupMessageDto nesnesini kullanarak işlemleri yapma
        groupMessageService.sendGroupMessage(groupMessageDto);
        // İşleme aldıktan sonra isteğe bağlı olarak diğer işlemler de yapılabilir
    }
}
