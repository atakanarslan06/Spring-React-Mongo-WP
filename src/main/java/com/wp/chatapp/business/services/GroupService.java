package com.wp.chatapp.business.services;

import com.wp.chatapp.business.dto.GroupDto;
import com.wp.chatapp.business.dto.GroupUserOperationDto;
import com.wp.chatapp.business.enums.GroupOperationType;
import com.wp.chatapp.dal.models.Group;
import com.wp.chatapp.dal.repositories.GroupRepository;
import com.wp.chatapp.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GroupService {
    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public List<Group> getAllGroups() {
        // Sadece aktif grupları al
        return groupRepository.findByIsActiveTrue();
    }

    public String createGroup(GroupDto groupDto) {
        Group group = new Group();
        group.setName(groupDto.getName());

        // Grup oluşturulduğunda adminUserId ve members listesini ayarla
        String adminUserId = groupDto.getAdminUserId();
        List<String> members = groupDto.getMembers(); // GrupDto'dan tüm üyeleri al

        // Admin kullanıcıyı da üyeler listesine başa ekle
        members.add(0, adminUserId);

        group.setAdminUserId(adminUserId);
        group.setMembers(members);
        group.setActive(true); // Grup oluşturulduğunda varsayılan olarak aktif olsun

        groupRepository.save(group);

        return "Group created successfully";
    }


    public Group getGroupById(String groupId) {
        // Belirli bir ID'ye sahip grubu al ve aktiflik durumunu kontrol et
        return groupRepository.findByIdAndActiveTrue(groupId)
                .orElseThrow(() -> new NotFoundException("Active group not found with id: " + groupId));
    }

    public String updateGroup(String groupId, GroupDto groupDto) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Group not find with id: " + groupId));

        group.setName(groupDto.getName());
        group.setMembers(groupDto.getMembers());

        groupRepository.save(group);

        return "Group updated successfully";
    }

    public String deactivateGroup(String groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Group not found with id:" + groupId));

        group.setActive(false);
        groupRepository.save(group);

        return "Group deactivated successfully";
    }

    public String manageGroupUsers(String groupId, GroupUserOperationDto dto) {
        // Grubu veritabanından al
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Group not found with id: " + groupId));

        List<String> members = group.getMembers();

        // İstek gövdesindeki işlem türüne göre kullanıcıları ekleyin veya kaldırın
        if (dto.getOperationType() == GroupOperationType.ADD_MEMBER) {
            for (String userId : dto.getUserIds()) {
                // Eğer kullanıcı daha önce gruba eklenmemişse
                if (!members.contains(userId)) {
                    // Kullanıcıyı gruba ekle
                    members.add(userId);
                }
            }
            group.setMembers(members);
            groupRepository.save(group);
            return "Users added to group successfully";
        } else if (dto.getOperationType() == GroupOperationType.REMOVE_MEMBER) {
            for (String userId : dto.getUserIds()) {
                // Kullanıcı listede varsa kaldır
                members.remove(userId);
            }
            group.setMembers(members);
            groupRepository.save(group);
            return "Users removed from group successfully";
        } else {
            return "Invalid operation type";
        }
    }

    public List<Group> getGroupsByUserId(String userId) {
        return groupRepository.findGroupsByMembersContains(userId);
    }

    public String leaveGroup(String groupId, String memberId) {
        try {
            // Grubun üyelerinden memberId'yi çıkar
            Group group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
            group.getMembers().remove(memberId);
            groupRepository.save(group);

            return "User successfully left the group.";
        } catch (Exception e) {
            // Hata durumunda isteğe bağlı olarak uygun bir şekilde işleyin
            e.printStackTrace();
            return "Failed to leave the group.";
        }
    }

    public String addMembersToGroup(String groupId, List<String> members) {
        try {
            // Grubu veritabanından al
            Group group = groupRepository.findById(groupId)
                    .orElseThrow(() -> new NotFoundException("Group not found with id: " + groupId));

            // Yeni üyeleri mevcut üyelere ekleyin (sadece mevcut üyelerde yoksa)
            for (String member : members) {
                if (!group.getMembers().contains(member)) {
                    group.getMembers().add(member);
                }
            }

            // Grubun mevcut üyelerini koruyun
            // Eski üyeleri silmeyin

            // Grubu veritabanına kaydet
            groupRepository.save(group);

            return "Members added to the group successfully";
        } catch (Exception e) {
            // Hata durumunda uygun bir şekilde işleyin
            e.printStackTrace();
            return "Failed to add members to the group.";
        }
    }
}