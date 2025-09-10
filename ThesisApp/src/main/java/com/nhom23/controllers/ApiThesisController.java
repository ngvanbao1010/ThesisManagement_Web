/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.nhom23.controllers;

import com.nhom23.pojo.Major;
import com.nhom23.pojo.Thesis;
import com.nhom23.pojo.User;
import com.nhom23.pojo.UserRole;
import com.nhom23.repositories.UserRepository;
import com.nhom23.services.PDFService;
import com.nhom23.services.ThesisService;
import com.nhom23.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiThesisController {

    @Autowired
    private ThesisService thesisService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PDFService pdfService;

    @PostMapping(path = "/secure/theses", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createThesisByStaff(
            @RequestBody Map<String, Object> body,
            HttpServletRequest request
    ) {
        try {
            String username = (String) request.getAttribute("username");

            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Không xác định được người dùng!");
            }

            User staff = userService.getUserByUsername(username);

            if (staff == null || staff.getUserRole() != UserRole.ACADEMIC_STAFF) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Chỉ giáo vụ mới có quyền tạo khóa luận!");
            }

            String title = (String) body.get("title");
            List<Integer> studentIds = (List<Integer>) body.get("studentIds");
            List<Integer> lecturerIds = (List<Integer>) body.get("lecturerIds");
            String fileKhoaluan = (String) body.get("fileThesis");
            String majorS = (String) body.get("major");
            Major major = Major.valueOf(majorS.trim().toUpperCase());

            if (lecturerIds == null || lecturerIds.size() > 2) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tối đa 2 giảng viên hướng dẫn!");
            }

            boolean success = thesisService.addThesisByStaff(title, lecturerIds, studentIds, fileKhoaluan, major);
            if (success) {
                return ResponseEntity.ok("Tạo khóa luận thành công!");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi khi tạo khóa luận!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống!");
        }
    }

    @GetMapping("/secure/theses/mine")
    public ResponseEntity<?> getThesesOfStudent(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        User student = userService.getUserByUsername(username);

        if (student == null || student.getUserRole() != UserRole.STUDENT) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không phải sinh viên!");
        }
        System.out.println(student.getId());
        Thesis list = thesisService.getThesesByStudentId(student.getId());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/secure/theses/supervise")
    public ResponseEntity<?> getThesesSupervisedByLecturer(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        User lecturer = userService.getUserByUsername(username);

        if (lecturer == null || lecturer.getUserRole() != UserRole.LECTURER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không phải giảng viên!");
        }

        List<Thesis> list = thesisService.getThesesByLecturerId(lecturer.getId());
        return ResponseEntity.ok(list);
    }

    @PostMapping(path = "/student/thesis/file", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> submitThesisFile(@RequestBody Map<String, Object> body,
            HttpServletRequest request) {
        try {
            String username = (String) request.getAttribute("username");
            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Không xác định người dùng!");
            }

            String fileUrl = body.get("fileUrl") != null ? body.get("fileUrl").toString() : null;
            boolean success = thesisService.submitThesisFile(username, fileUrl);
            if (success) {
                return ResponseEntity.ok(Map.of("message", "Nộp khóa luận thành công!"));

            }
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống!");
        }
        return null;
    }

    @GetMapping("/secure/score-per-year")
    public ResponseEntity<?> getAvgScorePerYear() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.getUserByUsername(auth.getName());

        if (user == null || (user.getUserRole() != UserRole.ACADEMIC_STAFF && user.getUserRole() != UserRole.ADMIN)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không có quyền thực hiện thao tác này.");
        }
        return ResponseEntity.ok(thesisService.getScorePerYear());
    }

    @GetMapping("/secure/by-major/{year}")
    public ResponseEntity<?> getThesisCountByMajor(@PathVariable("year") int year) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.getUserByUsername(auth.getName());

        if (user == null || (user.getUserRole() != UserRole.ACADEMIC_STAFF && user.getUserRole() != UserRole.ADMIN)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không có quyền thực hiện thao tác này.");
        }
        return ResponseEntity.ok(thesisService.getThesisCountByMajor(year));
    }

    @GetMapping("/secure/thesis/{id}/export-pdf")
    public ResponseEntity<byte[]> exportThesisReport(@PathVariable("id") int thesisId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.getUserByUsername(auth.getName());

        if (user == null || (user.getUserRole() != UserRole.ACADEMIC_STAFF && user.getUserRole() != UserRole.ADMIN)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        byte[] pdfBytes = pdfService.exportThesisReport(thesisId);

        if (pdfBytes == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "thesis_report_" + thesisId + ".pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/secure/theses")
    public ResponseEntity<?> getAllTheses() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Không xác định người dùng!");
        }

        String username = auth.getName();
        User user = userRepository.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Không tìm thấy người dùng.");
        }

        // Chỉ cho giáo vụ xem tất cả khóa luận
        if (user.getUserRole() != UserRole.ACADEMIC_STAFF) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không có quyền!");
        }

        return ResponseEntity.ok(thesisService.getAllTheses());
    }

}
