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
            existingUser.setProfile_picture(userDto.getProfile_picture());
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
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new NotFoundException("User not found with phone number: " + phoneNumber));
        if (!user.isActive()) {
            throw new NotFoundException("User found with phone number: " + phoneNumber);
        }
        return user;
    }

    public String sendFriendRequest(String senderId, String receiverId) {
        try {
            // Sender'ın bilgilerini veritabanından al
            User sender = userRepository.findById(senderId)
                    .orElseThrow(() -> new NotFoundException("Sender not found"));

            // Receiver'ın bilgilerini veritabanından al
            User receiver = userRepository.findById(receiverId)
                    .orElseThrow(() -> new NotFoundException("Receiver not found"));

            // Arkadaşlık isteği gönderilmiş mi veya zaten arkadaşlar mı?
            if (areFriendsOrRequestExists(sender, receiver)) {
                return "Friend request already sent or already friends";
            }

            // Arkadaşlık isteği gönderme işlemini gerçekleştir
            sendFriendRequestAndNotify(sender, receiver);

            return "Friend request sent successfully";
        } catch (Exception e) {
            throw new RuntimeException("Error while sending friend request: " + e.getMessage());
        }
    }


    private boolean areFriendsOrRequestExists(User sender, User receiver) {
        // Sender'ın arkadaş listesinde receiver var mı?
        if (sender.getFriends() != null && sender.getFriends().contains(receiver.getId())) {
            return true; // Zaten arkadaşlar
        }

        // Receiver'ın arkadaş listesinde sender var mı?
        return receiver.getFriends() != null && receiver.getFriends().contains(sender.getId()); // Zaten arkadaşlar

        // Diğer durumlar: Arkadaşlık isteği gönderilmiş değil
    }

    private void sendFriendRequestAndNotify(User sender, User receiver) {
        // Arkadaşlık isteği oluştur
        FriendshipRequest friendshipRequest = new FriendshipRequest();
        friendshipRequest.setSenderId(sender.getId());
        friendshipRequest.setReceiverId(receiver.getId());
        friendshipRequest.setStatus(RequestStatus.PENDING); // İsteğin başlangıç durumu bekleyen olarak ayarlandı

        // İsteği veritabanına kaydet
        friendshipRequestRepository.save(friendshipRequest);
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

    public List<User> getFriends(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        List<String> friendIds = user.getFriends();
        return userRepository.findAllById(friendIds);
    }

    public List<FriendshipRequest> getFriendshipRequests(String userId) {

        // Kullanıcının gelen arkadaşlık isteklerini al
        List<FriendshipRequest> friendshipRequests = friendshipRequestRepository.findByReceiverIdAndStatus(userId, RequestStatus.PENDING);

        // Her bir arkadaşlık isteği için gönderenin kullanıcı adını al ve ayarla
        for (FriendshipRequest request : friendshipRequests) {
            String senderUsername = userRepository.findById(request.getSenderId())
                    .map(User::getUsername)
                    .orElse("Unknown User");
            request.setSenderUsername(senderUsername);
        }

        return friendshipRequests;
    }


    public User findByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.orElseThrow(() -> new NotFoundException("User not found"));
    }

    public String deleteFriend(String userId, String friendId) {
        // Kullanıcıyı ve arkadaşı veritabanından al
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        // Arkadaşın veritabanında var mı kontrol et
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new NotFoundException("Friend not found with id: " + friendId));

        // Kullanıcının arkadaşı olarak ekli olup olmadığını kontrol et
        if (!user.getFriends().contains(friendId)) {
            throw new NotFoundException("Friend with id " + friendId + " is not a friend of user with id " + userId);
        }

        // Kullanıcıyı arkadaş listesinden çıkar
        user.getFriends().remove(friendId);
        userRepository.save(user);

        // Arkadaşın arkadaş listesinden kullanıcıyı çıkar
        friend.getFriends().remove(userId);
        userRepository.save(friend);

        return "Friend deleted successfully";
    }
}


