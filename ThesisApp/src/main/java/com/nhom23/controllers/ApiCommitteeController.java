/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.nhom23.controllers;

import com.nhom23.pojo.Committee;
import com.nhom23.pojo.CommitteeMember;
import com.nhom23.pojo.User;
import com.nhom23.pojo.UserRole;
import com.nhom23.repositories.UserRepository;
import com.nhom23.services.CommitteeService;
import com.nhom23.services.EmailService;
import com.nhom23.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import static java.lang.ProcessBuilder.Redirect.to;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiCommitteeController {

    @Autowired
    private CommitteeService committeeService;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;

    @PostMapping(path = "/secure/committees", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCommittee(@RequestBody Map<String, Object> body) {
        try {
            String name = (String) body.get("name");
            String defenseDateStr = (String) body.get("defenseDate");
            List<Integer> memberIds = (List<Integer>) body.get("memberIds");
            List<Integer> thesisIds = (List<Integer>) body.get("thesisIds");

            if (name == null || defenseDateStr == null || memberIds == null || thesisIds == null) {
                return ResponseEntity.badRequest().body("Thiếu dữ liệu bắt buộc!");
            }

            if (memberIds.size() < 3 || memberIds.size() > 5) {
                return ResponseEntity.badRequest().body("Phải có từ 3 đến 5 thành viên!");
            }

            LocalDateTime defenseDate = LocalDateTime.parse(defenseDateStr);
            boolean success = committeeService.createCommittee(name, defenseDate, memberIds, thesisIds);
            
            return success
                    ? ResponseEntity.ok("Tạo hội đồng thành công!")
                    : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi lưu vào cơ sở dữ liệu!");

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống!");
        }
    }

    @PutMapping("/secure/committees/lock/{id}")
    public ResponseEntity<?> lockCommittee(@PathVariable int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth.getName());
        User user = userRepository.getUserByUsername(auth.getName());
        System.out.println(user);

        if (user == null || user.getUserRole() != UserRole.ACADEMIC_STAFF) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không có quyền thực hiện thao tác này.");
        }

        boolean result = committeeService.lockCommittee(id);
        if (result) {
            return ResponseEntity.ok("Hội đồng đã được khoá và các khoá luận đã chuyển trạng thái.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy hội đồng.");
        }
    }
    
    @GetMapping("/secure/committees")
    public ResponseEntity<?> getCommittees() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Không tìm thấy người dùng.");
        }

        if (user.getUserRole() == UserRole.ACADEMIC_STAFF) {
            return ResponseEntity.ok(committeeService.getCommittees());
        }
        return null;
        
    }

}
