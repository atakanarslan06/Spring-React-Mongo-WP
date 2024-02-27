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

    public MessageDto getLastMessageByUserId(String userId) {
        // Kullanıcının gönderdiği son mesajı al
        List<Message> sentMessages = messageRepository.findBySenderIdOrderByTimestampDesc(userId);
        // Kullanıcının aldığı son mesajı al
        List<Message> receivedMessages = messageRepository.findByReceiverIdOrderByTimestampDesc(userId);

        // Gönderilen ve alınan mesajların tarihlerini karşılaştırarak en yeni mesajı belirle
        LocalDateTime latestSentMessageTime = sentMessages.isEmpty() ? null : sentMessages.get(0).getTimestamp();
        LocalDateTime latestReceivedMessageTime = receivedMessages.isEmpty() ? null : receivedMessages.get(0).getTimestamp();

        if (latestSentMessageTime == null && latestReceivedMessageTime == null) {
            // Kullanıcının gönderdiği veya aldığı hiç mesaj yoksa null dön
            return null;
        } else if (latestSentMessageTime == null) {
            // Kullanıcının sadece aldığı mesajlar varsa, en son aldığı mesajı dön
            return convertToDto(receivedMessages.get(0));
        } else if (latestReceivedMessageTime == null) {
            // Kullanıcının sadece gönderdiği mesajlar varsa, en son gönderdiği mesajı dön
            return convertToDto(sentMessages.get(0));
        } else {
            // Hem gönderilen hem de alınan mesajlar varsa, en yeni mesajı belirle
            LocalDateTime latestMessageTime = latestSentMessageTime.isAfter(latestReceivedMessageTime) ?
                    latestSentMessageTime : latestReceivedMessageTime;

            // En yeni mesajın bilgilerini dön
            Message latestMessage = latestSentMessageTime.isAfter(latestReceivedMessageTime) ?
                    sentMessages.get(0) : receivedMessages.get(0);
            return convertToDto(latestMessage);
        }
    }

}
