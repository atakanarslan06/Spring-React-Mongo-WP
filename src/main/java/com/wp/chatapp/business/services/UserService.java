package com.wp.chatapp.business.services;

import com.wp.chatapp.business.dto.UserDto;
import com.wp.chatapp.dal.models.User;
import com.wp.chatapp.dal.repositories.UserRepository;
import com.wp.chatapp.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String createUser(UserDto userDto) {
        try {
            User user = User.builder()
                    .username(userDto.getUsername())
                    .email(userDto.getEmail())
                    .password(userDto.getPassword())
                    .phoneNumber(userDto.getPhoneNumber())
                    .build();
            user.setActive(true);
            userRepository.save(user);
            return "User Created Successfully";
        } catch (Exception e) {
            return "User Not Created";
        }
    }

    public String updateUser(String id, UserDto userDto) {
        Optional<User> existingUserOptional = userRepository.findById(id);
        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            existingUser.setUsername(userDto.getUsername());
            existingUser.setEmail(userDto.getEmail());
            existingUser.setPassword(userDto.getPassword());
            userRepository.save(existingUser);
            return "User Updated Successfully";
        } else {
            return "User Not Found";
        }
    }

    public String activateUser(String userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setActive(true);
            userRepository.save(user);
            return "User activated successfully";
        } else {
            return "User not found";
        }
    }

    public String deactivateUser(String userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setActive(false);
            userRepository.save(user);
            return "User deactivated successfully";
        } else {
            return "User not found";
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {

        return userRepository.findById(id);
    }

    public User findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new NotFoundException("User not found with phone number: " + phoneNumber));
    }

    public String acceptFriendRequest(String userId, String friendId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id:" + userId));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new NotFoundException("Friend not found with id:" + friendId));

        // Arkadaş listelerini kontrol et ve başlat
        if (user.getFriends() == null) {
            user.setFriends(new ArrayList<>());
        }
        if (user.getFriendRequests() == null) {
            user.setFriendRequests(new ArrayList<>());
        }
        if (friend.getFriends() == null) {
            friend.setFriends(new ArrayList<>());
        }
        if (friend.getFriendRequests() == null) {
            friend.setFriendRequests(new ArrayList<>());
        }

        // Gönderenin friendRequests listesinden alıcının ID'sini kaldırma
        user.getFriendRequests().remove(friendId);

        // Alıcının friendRequests listesinden gönderenin ID'sini kaldırma
        friend.getFriendRequests().remove(userId);

        // Her iki kullanıcının da friend listesine karşı tarafın ID'sini ekleme
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);

        // Veritabanına değişiklikleri kaydet
        userRepository.save(user);
        userRepository.save(friend);

        return "Friend request accepted successfully";
    }

    public String rejectFriendRequest() {
        return "Friend request rejected successfully";
    }

    public String sendFriendRequest(String senderId, String receiverPhoneNumber) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new NotFoundException("Sender not found"));

        User receiver = userRepository.findByPhoneNumber(receiverPhoneNumber)
                .orElseThrow(() -> new NotFoundException("Receiver not found"));

        // Gönderen ve alıcı zaten arkadaşlarsa veya istek zaten gönderilmişse, tekrar istek gönderilmez
        if ((sender.getFriends() != null && sender.getFriends().contains(receiver.getId())) ||
                (sender.getFriendRequests() != null && sender.getFriendRequests().contains(receiver.getId()))) {
            return "Friend request already sent or already friends";
        }

        // Alıcı kullanıcıya arkadaşlık isteği gönderme
        if (receiver.getFriendRequests() == null) {
            receiver.setFriendRequests(new ArrayList<>());
        }

        // Aynı kullanıcıya birden fazla istek gönderilmesini engellemek için kontrol
        if (!receiver.getFriendRequests().contains(sender.getId())) {
            receiver.getFriendRequests().add(sender.getId());
            userRepository.save(receiver);

            // Receiver kullanıcısına bildirim gönderme işlemi
            String notificationMessage = "You have received a friend request from " + sender.getUsername();
            NotificationService notificationService = new NotificationService(userRepository);
            notificationService.sendNotification(receiver.getId(), notificationMessage);

            return "Friend request sent successfully";
        } else {
            return "Friend request already sent";
        }
    }
}
