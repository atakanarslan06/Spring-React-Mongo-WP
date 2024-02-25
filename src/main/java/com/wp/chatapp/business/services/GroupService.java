package com.wp.chatapp.business.services;

import com.wp.chatapp.business.dto.GroupDto;
import com.wp.chatapp.dal.models.Group;
import com.wp.chatapp.dal.repositories.GroupRepository;
import com.wp.chatapp.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {
    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }
    public List<Group> getAllGroups(){
        return groupRepository.findAll();
    }

    public String createGroup(GroupDto groupDto){
        Group group = new Group();
        group.setName(groupDto.getName());

        // Grup oluşturulduğunda adminUserId ve members listesini ayarla
        String adminUserId = groupDto.getAdminUserId();
        List<String> members = new ArrayList<>();
        members.add(adminUserId); // Admin, üyeler listesine ekleniyor

        group.setAdminUserId(adminUserId);
        group.setMembers(members);
        group.setActive(true); // Grup oluşturulduğunda varsayılan olarak aktif olsun

        groupRepository.save(group);

        return "Group created successfully";
    }



    public Group getGroupById(String groupId){
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Group not find with id: " + groupId));
    }

    public String updateGroup(String groupId, GroupDto groupDto ){
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Group not find with id: " + groupId));

        group.setName(groupDto.getName());
        group.setMembers(groupDto.getMembers());

         groupRepository.save(group);

         return "Group updated successfully";
    }

    public String addUsersToGroup(String groupId, List<String> userIds) {
        // Grubu veritabanından al
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Group not found with id: " + groupId));

        // Gruba eklenen her kullanıcı için
        for (String userId : userIds) {
            // Eğer kullanıcı daha önce gruba eklenmemişse
            if (!group.getMembers().contains(userId)) {
                // Kullanıcıyı gruba ekle
                group.getMembers().add(userId);
            }
        }

        // Değişiklikleri veritabanına kaydet
        groupRepository.save(group);

        return "Users added to group successfully";
    }
    public String deactivateGroup(String groupId){
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Group not found with id:" +groupId));

        group.setActive(false);
        groupRepository.save(group);

        return "Group deactivated successfully";
    }

    public String removeUserFromGroup(String groupId, String userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Group not found with id: " + groupId));

        List<String> members = group.getMembers();

        // Kullanıcı listede varsa kaldır
        if (members.contains(userId)) {
            members.remove(userId);
            group.setMembers(members);
            groupRepository.save(group);
            return "User removed from group successfully";
        } else {
            return "User is not a member of the group";
        }
    }
}
