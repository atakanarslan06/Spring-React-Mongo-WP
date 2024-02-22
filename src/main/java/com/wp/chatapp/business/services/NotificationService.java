package com.wp.chatapp.business.services;

import com.wp.chatapp.dal.models.User;
import com.wp.chatapp.dal.repositories.UserRepository;
import com.wp.chatapp.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NotificationService {
    private final UserRepository userRepository;

    public NotificationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void sendNotification(String userId, String message){
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Kullanıcıya bildirim gönderme işlemi burada gerçekleştirilir
            System.out.println("Notification sent to user " + user.getUsername() + ": " + message);
        } else {
            throw new NotFoundException("User with id " + userId + " not found");
        }

    }
}
