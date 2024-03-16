package com.wp.chatapp.business.services;

import com.wp.chatapp.business.dto.GroupMessageDto;
import com.wp.chatapp.dal.models.GroupMessage;
import com.wp.chatapp.dal.repositories.GroupMessageRepository;
import com.wp.chatapp.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupMessageService {
    private final GroupMessageRepository groupMessageRepository;

    public GroupMessageService(GroupMessageRepository groupMessageRepository) {
        this.groupMessageRepository = groupMessageRepository;
    }

    public List<GroupMessageDto> getGroupMessagesByGroupId(String groupId) {
        List<GroupMessage> groupMessages = groupMessageRepository.findByGroupId(groupId);
        return groupMessages.stream()
                .filter(groupMessage -> !groupMessage.isDeleted())
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private GroupMessageDto convertToDto(GroupMessage groupMessage) {
        return GroupMessageDto.builder()
                .id(groupMessage.getId())
                .groupId(groupMessage.getGroupId())
                .senderId(groupMessage.getSenderId())
                .content(groupMessage.getContent())
                .timestamp(groupMessage.getTimestamp())
                .build();
    }
    public void sendGroupMessage(GroupMessageDto groupMessageDto){
        GroupMessage groupMessage = GroupMessage.builder()
                .groupId(groupMessageDto.getGroupId())
                .senderId(groupMessageDto.getSenderId())
                .content(groupMessageDto.getContent())
                .timestamp(LocalDateTime.now())
                .build();
        groupMessageRepository.save(groupMessage);
    }

    public void deleteGroupMessage(String messageId) {
        GroupMessage message = groupMessageRepository.findById(messageId)
                .orElseThrow(() -> new NotFoundException("Message not found with id: " + messageId));
        message.setDeleted(true);
        groupMessageRepository.save(message);
    }

}
