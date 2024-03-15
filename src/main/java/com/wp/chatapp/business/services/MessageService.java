package com.wp.chatapp.business.services;

import com.wp.chatapp.business.dto.MessageDto;
import com.wp.chatapp.dal.models.Message;
import com.wp.chatapp.dal.repositories.MessageRepository;
import com.wp.chatapp.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
                .filter(message -> !message.isDeleted()) // Silinmemiş mesajları filtrele
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
                .deleted(message.isDeleted())
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
            MessageDto lastReceivedMessageDto = convertToDto(receivedMessages.get(0));
            if (!receivedMessages.get(0).isDeleted()) {
                return lastReceivedMessageDto;
            } else {
                // Eğer en son alınan mesaj silinmişse, bir önceki mesajı getir
                return getNextNonDeletedMessage(userId);
            }
        } else if (latestReceivedMessageTime == null) {
            // Kullanıcının sadece gönderdiği mesajlar varsa, en son gönderdiği mesajı dön
            MessageDto lastSentMessageDto = convertToDto(sentMessages.get(0));
            if (!sentMessages.get(0).isDeleted()) {
                return lastSentMessageDto;
            } else {
                // Eğer en son gönderilen mesaj silinmişse, bir önceki mesajı getir
                return getNextNonDeletedMessage(userId);
            }
        } else {
            // En yeni mesajın bilgilerini dön
            Message latestMessage = latestSentMessageTime.isAfter(latestReceivedMessageTime) ?
                    sentMessages.get(0) : receivedMessages.get(0);

            // Mesajın silinip silinmediğini kontrol et
            if (!latestMessage.isDeleted()) {
                return convertToDto(latestMessage);
            } else {
                // Eğer mesaj silinmişse, bir önceki mesajı dön
                return getNextNonDeletedMessage(userId);
            }
        }
    }

    public MessageDto getNextNonDeletedMessage(String userId) {
        List<Message> messages = messageRepository.findByReceiverIdOrderByTimestampDesc(userId);
        for (Message message : messages) {
            if (!message.isDeleted()) {
                return convertToDto(message);
            }
        }
        return null;
    }

    public void deleteMessage(String messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NotFoundException("Message not found with id: " + messageId));
        message.setDeleted(true);
        messageRepository.save(message);
    }


    public List<MessageDto> getLastMessagesForEachUser(String userId) {
        return messageRepository.getLastMessagesForEachUser(userId);
    }
}
