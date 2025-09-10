/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.nhom23.controllers;

import com.nhom23.pojo.EvaluationCriteria;
import com.nhom23.pojo.EvaluationDetail;
import com.nhom23.pojo.Thesis;
import com.nhom23.pojo.User;
import com.nhom23.pojo.UserRole;
import com.nhom23.repositories.CommitteeMemberRepository;
import com.nhom23.repositories.ThesisRepository;
import com.nhom23.repositories.UserRepository;
import com.nhom23.services.EvaluationDetailService;
import com.nhom23.services.EvaluationService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/api/secure")
public class ApiEvaluationController {

    @Autowired
    private EvaluationService evaluationService;
    @Autowired
    private EvaluationDetailService evaluationDetailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommitteeMemberRepository committeeMemberRepository;
    @Autowired
    private ThesisRepository thesisRepository;

    @PostMapping("/criteria")
    public ResponseEntity<?> addCriterion(@RequestBody Map<String, String> body) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.getUserByUsername(username);

        if (user.getUserRole() != UserRole.ACADEMIC_STAFF) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không có quyền tạo tiêu chí.");
        }
        String criterion = body.get("criterion");
        EvaluationCriteria ec = evaluationService.addCriterion(criterion);
        return ResponseEntity.ok(ec);
    }
    @GetMapping("/criteria")
public ResponseEntity<?> getCriteria() {
    return ResponseEntity.ok(evaluationService.getAllCriteria());
}

    @PostMapping("/detail")
    public ResponseEntity<?> addEvaluationDetail(@RequestBody Map<String, Object> body) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Không tìm thấy người dùng.");
        }
        System.out.println(user.getUserRole());

        if (user.getUserRole() != UserRole.LECTURER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Chỉ giảng viên mới được chấm điểm.");
        }
        // Validation
        if (body.get("thesisId") == null
                || body.get("lecturerId") == null
                || body.get("criterionId") == null
                || body.get("score") == null) {
            return ResponseEntity.badRequest().body("Tất cả các trường (thesisId, lecturerId, criterionId, score) đều bắt buộc.");
        }

        try {

            int thesisId = Integer.parseInt(body.get("thesisId").toString());
            int lecturerId = Integer.parseInt(body.get("lecturerId").toString());
            int criterionId = Integer.parseInt(body.get("criterionId").toString());
            double score = Double.parseDouble(body.get("score").toString());
            if (!committeeMemberRepository.isReviewerOfThesis(user.getId(), thesisId)) {

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không phải là giảng viên phản biện của khóa luận này.");
            }
            if (evaluationDetailService.exists(thesisId, lecturerId, criterionId)) {
                throw new IllegalStateException("Tiêu chí này đã được chấm.");
            }
            Thesis thesis = thesisRepository.getThesisById(thesisId);
            if (!thesis.getStatus().equals("PENDING")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Khóa luận không còn ở trạng thái chờ chấm (PENDING).");
            }

            EvaluationDetail detail = evaluationService.addEvaluationDetail(thesisId, lecturerId, criterionId, score);
            return ResponseEntity.ok(detail);

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Định dạng số không hợp lệ trong tham số request.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi tạo chi tiết đánh giá: " + e.getMessage());
        }
    }

    @GetMapping("/thesis/{id}/criteria")
    public ResponseEntity<?> getCriteriaByThesis(@PathVariable("id") int thesisId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Không tìm thấy người dùng.");
        }

        if (user.getUserRole() == UserRole.ACADEMIC_STAFF) {
            return ResponseEntity.ok(evaluationService.getEvaluationDetailsForThesis(thesisId));
        }
        if (user.getUserRole() == UserRole.ADMIN) {
            return ResponseEntity.ok(evaluationService.getAllCriteria());
        }

        if (!committeeMemberRepository.isMemberOfThesisCommittee(user.getId(), thesisId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không thuộc hội đồng của khóa luận này.");
        }

        return ResponseEntity.ok(evaluationService.getEvaluationDetailsForThesis(thesisId));
    }

    @GetMapping("/thesis/{id}/average-score")
    public ResponseEntity<?> getAverageScore(@PathVariable("id") int thesisId) {
        double avg = evaluationDetailService.getScoreOfThesis(thesisId);
        return ResponseEntity.ok(avg);
    }
}
