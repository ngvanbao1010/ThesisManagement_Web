/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom23.services;

import com.nhom23.pojo.User;
import com.nhom23.pojo.UserRole;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

public interface UserService extends UserDetailsService {

    User getUserByUsername(String username);

    User getUserById(int id);

    List<User> getUsers();

    User addUserFromFormData(MultipartFile avatar, String firstName, String lastName, String email,
            String phone, String username, String password, String role);

    boolean authenticate(String username, String password);

    boolean updateUser(User u);

    boolean changePassword(int userId, String oldPass, String newPass);

    List<User> getUsersByRole(UserRole role);
    
    List<User> getAllowedLecturersForCommittee(List<Integer> thesisIds);


}
