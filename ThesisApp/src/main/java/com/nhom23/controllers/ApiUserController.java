package com.nhom23.controllers;

import com.nhom23.pojo.Thesis;
import com.nhom23.pojo.User;
import com.nhom23.pojo.UserRole;
import com.nhom23.repositories.UserRepository;
import com.nhom23.services.ThesisService;
import com.nhom23.services.UserService;
import com.nhom23.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiUserController {

    @Autowired
    private UserService userDetailsService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ThesisService thesisService;

    @PostMapping(path = "/register",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> register(
            @RequestParam("avatar") MultipartFile avatar,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("role") String role) {

        User u = this.userDetailsService.addUserFromFormData(avatar, firstName, lastName, email, phone, username, password, role);
        return new ResponseEntity<>(u, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User u) {
        if (this.userDetailsService.authenticate(u.getUsername(), u.getPassword())) {
            try {
                String token = JwtUtils.generateToken(u.getUsername());
                return ResponseEntity.ok().body(Collections.singletonMap("token", token));
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Lỗi khi tạo JWT");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai thông tin đăng nhập");
    }

    @RequestMapping("/secure/profile")
    @ResponseBody
    public ResponseEntity<User> getProfile(Principal principal) {
        return new ResponseEntity<>(this.userDetailsService.getUserByUsername(principal.getName()), HttpStatus.OK);
    }

    @PostMapping("/secure/change-password")
    public ResponseEntity<?> userChangepassword(
            @RequestParam("old-password") String oldPassword,
            @RequestParam("new-password") String newPassword) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth.getName());
        User user = userRepository.getUserByUsername(auth.getName());
        System.out.println(user);
        boolean success = userDetailsService.changePassword(user.getId(), oldPassword, newPassword);
        if (success) {
            return ResponseEntity.ok("ok");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Đổi mật khẩu thất bại");
        }
    }

    @GetMapping("/secure/users")
    public ResponseEntity<?> getThesesOfStudent(@RequestParam("role") UserRole role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.getUserByUsername(auth.getName());

        if (user == null || user.getUserRole() != UserRole.ACADEMIC_STAFF) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không có quyền!");
        }
        List<User> list = userService.getUsersByRole(role);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/secure/lecturers/allowed")
    public ResponseEntity<List<User>> getAllowedLecturers(@RequestParam List<Integer> thesisIds) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.getUserByUsername(auth.getName());

        if (user == null || user.getUserRole() != UserRole.ACADEMIC_STAFF) {
            
        }
        return ResponseEntity.ok(userService.getAllowedLecturersForCommittee(thesisIds));
    }

}
