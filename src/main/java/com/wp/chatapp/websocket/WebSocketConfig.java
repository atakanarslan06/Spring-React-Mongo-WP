package com.wp.chatapp.websocket;

import com.wp.chatapp.business.services.GroupMessageService;
import com.wp.chatapp.business.services.MessageService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final MessageService messageService;
    private final GroupMessageService groupMessageService;

    public WebSocketConfig(MessageService messageService, GroupMessageService groupMessageService) {
        this.messageService = messageService;
        this.groupMessageService = groupMessageService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ChatWebSocketHandler(messageService,groupMessageService), "/chat").setAllowedOrigins("*");
    }
}
