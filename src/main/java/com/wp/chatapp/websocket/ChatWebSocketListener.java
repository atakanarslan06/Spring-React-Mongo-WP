package com.wp.chatapp.websocket;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
@Component
public class ChatWebSocketListener extends TextWebSocketHandler {

    private final ChatWebSocketHandler chatWebSocketHandler;

    public ChatWebSocketListener(ChatWebSocketHandler chatWebSocketHandler) {
        this.chatWebSocketHandler = chatWebSocketHandler;
    }
    @Override
    public void afterConnectionEstablished(
            WebSocketSession session) {
        // Yeni bir bağlantı kurulduğunda
        System.out.println("Yeni bir bağlantı kuruldu: " + session.getId());
    }

    @Override
    protected void handleTextMessage(
            @NonNull WebSocketSession session,
            @NonNull TextMessage message)
            throws Exception {
        // Gelen mesajları işlemek için WebSocketHandler'a iletiyoruz
        chatWebSocketHandler.handleTextMessage(session, message);
    }

    @Override
    public void afterConnectionClosed(
            @NonNull WebSocketSession session,
           @NonNull CloseStatus status) {
        // Bir bağlantı kapandığında
        System.out.println("Bağlantı kapatıldı: " + session.getId() + " - Durum: " + status);
    }

}
