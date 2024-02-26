package com.wp.chatapp.business.services;

import com.wp.chatapp.business.dto.UserDto;
import com.wp.chatapp.business.enums.RequestStatus;
import com.wp.chatapp.dal.models.FriendshipRequest;
import com.wp.chatapp.dal.models.User;
import com.wp.chatapp.dal.repositories.FriendshipRequestRepository;
import com.wp.chatapp.dal.repositories.UserRepository;
import com.wp.chatapp.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final FriendshipRequestRepository friendshipRequestRepository;

    public UserService(UserRepository userRepository, FriendshipRequestRepository friendshipRequestRepository) {
        this.userRepository = userRepository;
        this.friendshipRequestRepository = friendshipRequestRepository;
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
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream()
                .filter(User::isActive)
                .collect(Collectors.toList());
    }


    public Optional<User> getUserById(String id) {
        return userRepository.findByIdAndActive(id, true);
    }


    public User findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new NotFoundException("User not found with phone number: " + phoneNumber));
    }

    public String sendFriendRequest(String senderId, String receiverId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new NotFoundException("Sender not found"));

        if (areFriendsOrRequestExists(sender, receiverId)) {
            return "Friend request already sent or already friends";
        }

        sendFriendRequestAndNotify(sender, receiverId);
        return "Friend request sent successfully";
    }

    private boolean areFriendsOrRequestExists(User sender, String receiverId) {
        // Arkadaşlık isteği veritabanında var mı kontrol edilir
        return friendshipRequestRepository.existsBySenderIdAndReceiverId(sender.getId(), receiverId);
    }

    private void sendFriendRequestAndNotify(User sender, String receiverId) {
        // Arkadaşlık isteği oluşturulur ve veritabanına kaydedilir
        FriendshipRequest friendshipRequest = new FriendshipRequest(sender.getId(), receiverId, RequestStatus.PENDING);
        friendshipRequestRepository.save(friendshipRequest);

        // Alıcı kullanıcıya bildirim gönderme işlemi
        String notificationMessage = "You have received a friend request from " + sender.getUsername();
        NotificationService notificationService = new NotificationService(userRepository);
        notificationService.sendNotification(receiverId, notificationMessage);
    }


    public String handleFriendRequest(String requestId, String userId, boolean accept) {
        // FriendshipRequest koleksiyonundan ilgili isteği bul
        FriendshipRequest friendshipRequest = friendshipRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Friendship request not found"));

        // İsteğin durumunu kabul edildi veya reddedildi olarak güncelle
        if (accept) {
            friendshipRequest.setStatus(RequestStatus.ACCEPTED);
        } else {
            friendshipRequest.setStatus(RequestStatus.REJECTED);
        }

        // Güncellenmiş istek nesnesini veritabanına kaydet
        friendshipRequestRepository.save(friendshipRequest);

        // İlgili kullanıcıyı bul
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id:" + userId));

        // Kabullenme durumunda arkadaş listesine ekle, reddetme durumunda istek listesinden kaldır
        if (accept) {
            // Arkadaş listesine ekle
            user.getFriends().add(friendshipRequest.getSenderId());

            // İsteği atan kullanıcının arkadaş listesine de eklenmeli
            User requester = userRepository.findById(friendshipRequest.getSenderId())
                    .orElseThrow(() -> new NotFoundException("User not found with id:" + friendshipRequest.getSenderId()));
            requester.getFriends().add(userId);
            userRepository.save(requester);
        } else {
            // Reddetme durumunda istek listesinden kaldır
            user.getFriendRequests().remove(requestId);
        }

        // Kullanıcıyı kaydet
        userRepository.save(user);

        // Başarılı mesajını döndür
        return "Friend request " + (accept ? "accepted" : "rejected") + " successfully";
    }


}
