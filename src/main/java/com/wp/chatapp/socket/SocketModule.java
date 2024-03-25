package com.wp.chatapp.socket;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.wp.chatapp.dal.models.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SocketModule {

    public SocketModule(SocketIOServer socketIOServer) {
        socketIOServer.addConnectListener(onConnected());
        socketIOServer.addDisconnectListener(onDisconnected());
        socketIOServer.addEventListener("send_message", Message.class, onMessageReceived());
    }

    private DataListener<Message> onMessageReceived(){
        return (senderClient, data, ackSender) -> {
            log.info(String.format("%s -> %s", senderClient.getSessionId(), data.getContent()));
            senderClient.getNamespace().getAllClients().forEach(
                    x->{
                        if (!x.getSessionId().equals(senderClient.getSessionId())){
                            x.sendEvent("get_message",data.getContent());
                        }
                    }
            );
        };
    }


    private ConnectListener onConnected(){
        return client ->{

            log.info(String.format("SocketID: %s connected", client.getSessionId().toString()));
        };
    }

    private DisconnectListener onDisconnected(){
        return client -> {
            log.info(String.format("SocketID: %s disconnected", client.getSessionId().toString()));
        };
    }

}
