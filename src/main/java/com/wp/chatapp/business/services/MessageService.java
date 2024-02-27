package com.wp.chatapp.business.services;

import com.wp.chatapp.business.dto.MessageDto;
import com.wp.chatapp.dal.models.Message;
import com.wp.chatapp.dal.repositories.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<MessageDto> getMessagesByUserId(String userId){
        List<Message> messages = messageRepository.findByReceiverIdOrSenderId(userId, userId);

        return messages.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    private MessageDto convertToDto(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .senderId(message.getSenderId())
                .receiverId(message.getReceiverId())
                .content(message.getContent())
                .timestamp(message.getTimestamp())
                .build();
    }
    public void sendMessage(MessageDto messageDto) {
        Message message = Message.builder()
                .senderId(messageDto.getSenderId())
                .receiverId(messageDto.getReceiverId())
                .content(messageDto.getContent())
                .timestamp(LocalDateTime.now())
                .build();
        messageRepository.save(message);
    }
}
