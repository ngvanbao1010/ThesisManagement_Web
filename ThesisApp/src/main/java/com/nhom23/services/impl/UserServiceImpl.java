/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom23.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nhom23.pojo.User;
import com.nhom23.pojo.UserRole;
import com.nhom23.repositories.UserRepository;
import com.nhom23.services.UserService;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public User getUserByUsername(String username) {
        return this.userRepo.getUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = this.getUserByUsername(username);
        if (u == null) {
            throw new UsernameNotFoundException("Invalid username!");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + u.getUserRole().name()));

        return new org.springframework.security.core.userdetails.User(
                u.getUsername(), u.getPassword(), authorities);
    }

    @Override
    public User addUserFromFormData(MultipartFile avatar, String firstName, String lastName,
            String email, String phone, String username,
            String password, String role) {
        try {
            if (avatar == null || avatar.isEmpty()) {
                throw new IllegalArgumentException("Avatar is required!");
            }

            User u = new User();
            u.setFirstName(firstName);
            u.setLastName(lastName);
            u.setEmail(email);
            u.setPhone(phone);
            u.setUsername(username);
            u.setPassword(this.passwordEncoder.encode(password));
            u.setUserRole(UserRole.valueOf(role.toUpperCase()));
            u.setActive(true);

            Map res = cloudinary.uploader().upload(avatar.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
            u.setAvatar(res.get("secure_url").toString());

            return this.userRepo.addUser(u);
        } catch (IOException ex) {
            Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public boolean authenticate(String username, String password) {
        User u = this.getUserByUsername(username);
        System.out.println("=== DEBUG AUTHENTICATE ===");
        System.out.println("User from DB: " + u);
        System.out.println("Raw password: " + password);
        System.out.println("Hashed from DB: " + (u != null ? u.getPassword() : "null"));
        if (u == null || password == null || u.getPassword() == null) {
            return false;
        }
        boolean result = this.passwordEncoder.matches(password, u.getPassword());
        System.out.println("Match result: " + result);
        return result;
    }

    @Override
    public User getUserById(int id) {
        return this.userRepo.getUserById(id);
    }

    @Override
    public List<User> getUsers() {
        return this.userRepo.getUsers();
    }

    @Override
    public boolean updateUser(User u) {
        try {
            User existing = this.userRepo.getUserById(u.getId());
            if (existing == null) {
                return false;
            }

            existing.setFirstName(u.getFirstName());
            existing.setLastName(u.getLastName());
            existing.setEmail(u.getEmail());
            existing.setPhone(u.getPhone());
            existing.setUserRole(u.getUserRole());
            existing.setActive(u.getActive());
            existing.setAvatar(u.getAvatar());

            return this.userRepo.updateUser(existing);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean changePassword(int userId, String oldPass, String newPass) {
        User u = this.userRepo.getUserById(userId);
        if (u == null) {
            return false;
        }

        if (!passwordEncoder.matches(oldPass, u.getPassword())) {
            return false;
        }

        String encodedNew = passwordEncoder.encode(newPass);
        return this.userRepo.changePassword(userId, encodedNew);
    }

    @Override
    public List<User> getUsersByRole(UserRole role) {
        return this.userRepo.getUsersByRole(role);
    }

    @Override
    public List<User> getAllowedLecturersForCommittee(List<Integer> thesisIds) {
        return userRepo.getLecturersNotSupervisorsOfTheses(thesisIds);
    }
}
