/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom23.services.impl;

import com.nhom23.pojo.Committee;
import com.nhom23.pojo.CommitteeMember;
import com.nhom23.pojo.CommitteeMember.Role;
import com.nhom23.pojo.CommitteeMemberId;
import com.nhom23.pojo.EvaluationDetail;
import com.nhom23.pojo.Status;
import com.nhom23.pojo.Thesis;
import com.nhom23.pojo.User;
import com.nhom23.pojo.UserRole;
import com.nhom23.repositories.CommitteeMemberRepository;
import com.nhom23.repositories.CommitteeRepository;
import com.nhom23.repositories.EvaluationDetailRepository;
import com.nhom23.repositories.ThesisRepository;
import com.nhom23.repositories.UserRepository;
import com.nhom23.services.CommitteeService;
import com.nhom23.services.EmailService;
import com.nhom23.services.ThesisService;
import com.nhom23.services.UserService;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CommitteeServiceImpl implements CommitteeService {

    @Autowired
    private CommitteeRepository committeeRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private CommitteeMemberRepository committeeMemberRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ThesisService thesisService;
    @Autowired
    private ThesisRepository thesisRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EvaluationDetailRepository evaluationDetailRepository;
    
    @Override
    public boolean createCommittee(String name, LocalDateTime defenseDate, List<Integer> memberIds, List<Integer> thesisIds) {
        if (memberIds == null || memberIds.size() < 3 || memberIds.size() > 5) {
            throw new IllegalArgumentException("Số lượng thành viên phải từ 3 đến 5!");
        }

        Committee c = new Committee();
        c.setName(name);
        c.setDefenseDate(defenseDate);
        c.setAvailable(true);

        committeeRepo.addCommittee(c);

        Set<CommitteeMember> members = new HashSet<>();
        for (int i = 0; i < memberIds.size(); i++) {
            Integer id = memberIds.get(i);
            User lecturer = userService.getUserById(id);
            if (lecturer != null && lecturer.getUserRole() == UserRole.LECTURER) {
                CommitteeMember m = new CommitteeMember();

                CommitteeMemberId cmId = new CommitteeMemberId();
                cmId.setCommitteeId(c.getId());
                cmId.setLecturerId(lecturer.getId());
                m.setId(cmId);

                m.setCommittee(c);

                m.setLecturer(lecturer);

                switch (i) {
                    case 0 ->
                        m.setRole(Role.CHAIRMAN);
                    case 1 ->
                        m.setRole(Role.SECRETARY);
                    case 2 ->
                        m.setRole(Role.REVIEWER);
                    default ->
                        m.setRole(Role.MEMBER);
                }

                members.add(m);
            }
        }

        if (members.size() < 3) {
            throw new IllegalArgumentException("Không đủ giảng viên hợp lệ!");
        }

        c.setMembers(members);

        Set<com.nhom23.pojo.Thesis> theses = new HashSet<>();
        for (Integer tid : thesisIds) {
            com.nhom23.pojo.Thesis t = thesisService.getThesisById(tid);
            if (t != null) {
                t.setCommittee(c); 
                t.setStatus(Status.DEFENDED);

                theses.add(t);
            }
        }

        if (theses.isEmpty()) {
            throw new IllegalArgumentException("Không có khóa luận hợp lệ nào!");
        }

        c.setTheses(theses); 
        committeeRepo.addCommittee(c);

        System.out.println("🎯 Committee: " + c.getName());
        System.out.println("🎯 Committee: " + c.getId());
        System.out.println("🎯 Committee: " + c.getMembers());

        for (CommitteeMember m : c.getMembers()) {
            if (m.getRole() == Role.REVIEWER) {
                User reviewer = m.getLecturer();
                String email = reviewer.getEmail();
                String subject = "[Thông báo] Bạn được phân công làm phản biện khóa luận";
                String body = String.format("""
            Xin chào %s %s,

            Bạn vừa được phân công làm phản biện cho Hội đồng bảo vệ khóa luận "%s".
            Thời gian bảo vệ: %s.

            Vui lòng kiểm tra hệ thống để biết thêm chi tiết.
            """,
                        reviewer.getLastName(),
                        reviewer.getFirstName(),
                        c.getName(),
                        c.getDefenseDate().toString()
                );

                emailService.sendSimpleEmail(email, subject, body);
            }
        }
        return true;
    }

    @Override
    public List<Committee> getCommittees() {
        return this.committeeRepo.getCommittees();
    }

    @Override
    public Committee getCommitteeById(int id) {
        return this.committeeRepo.getCommitteeById(id);
    }

    @Override
    public boolean addCommittee(Committee c) {
        return this.committeeRepo.addCommittee(c);
    }

    @Override
    public boolean updateCommittee(Committee c) {
        return this.committeeRepo.updateCommittee(c);
    }

    @Override
    public boolean deleteCommittee(int id) {
        return this.committeeRepo.deleteCommittee(id);
    }

    @Override
    public boolean lockCommittee(int committeeId) {
        boolean locked = committeeRepo.lockCommittee(committeeId);

        if (locked) {
            List<Thesis> theses = committeeRepo.getThesisById(committeeId);

            for (Thesis thesis : theses) {
                int thesisId = thesis.getId();
                List<User> users = new ArrayList<>(thesis.getStudents()); 

                List<EvaluationDetail> details = evaluationDetailRepository.findByThesisId(thesisId);
                double averageScore = details.stream()
                        .mapToDouble(EvaluationDetail::getScore)
                        .average()
                        .orElse(0.0);

                for (User student : users) {
                    String studentEmail = student.getEmail();
                    String subject = "🎓 Kết quả bảo vệ khóa luận tốt nghiệp";
                    String content = "Chào bạn " + student.getFirstName()+" "+student.getLastName() + ",\n\n"
                            + "Bạn đã hoàn thành bảo vệ khóa luận với điểm trung bình: " + averageScore + "\n"
                            + "Vui lòng đăng nhập hệ thống để xem chi tiết.\n\n"
                            + "Trân trọng.";

                    emailService.sendSimpleEmail(studentEmail, subject, content);
                }
            }
        }

        return locked;
    }
}
