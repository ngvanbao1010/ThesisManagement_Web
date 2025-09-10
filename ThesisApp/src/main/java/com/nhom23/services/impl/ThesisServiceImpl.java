/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom23.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nhom23.pojo.Major;
import com.nhom23.pojo.Status;
import com.nhom23.pojo.Thesis;
import com.nhom23.pojo.User;
import com.nhom23.pojo.UserRole;
import com.nhom23.repositories.ThesisRepository;
import com.nhom23.services.ThesisService;
import com.nhom23.services.UserService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ThesisServiceImpl implements ThesisService {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private UserService userService;
    @Autowired
    private ThesisRepository thesisRepo;

    @Override
    public List<Thesis> getAllTheses() {
        return this.thesisRepo.getTheses();
    }

    @Override
    public Thesis getThesesByStudentId(int studentId) {
        return this.thesisRepo.getThesisByStudentId(studentId);
    }

    @Override
    public List<Thesis> getThesesByLecturerId(int lecturerId) {
        return this.thesisRepo.getThesesByLecturerId(lecturerId);
    }

    @Override
    public Thesis getThesisById(int id) {
        return this.thesisRepo.getThesisById(id);
    }

    @Override
    public boolean addThesis(Thesis t) {
        return this.thesisRepo.addThesis(t);
    }

    @Override
    public boolean updateThesis(Thesis t) {
        return this.thesisRepo.updateThesis(t);
    }

    @Override
    public boolean deleteThesis(int id) {
        return this.thesisRepo.deleteThesis(id);
    }

    @Override
    public boolean addThesisByStaff(String title, List<Integer> lecturerIds, List<Integer> studentIds, String fileThesis, Major major) {
        if (title == null || title.trim().length() < 5) {
            throw new IllegalArgumentException("Tiêu đề khóa luận tối thiểu 5 ký tự.");
        }

        if (studentIds == null || studentIds.isEmpty()) {
            throw new IllegalArgumentException("Phải chọn ít nhất 1 sinh viên.");
        }
        Set<Integer> studentIdSet = new LinkedHashSet<>(studentIds);
        Set<Integer> lecturerIdSet = new LinkedHashSet<>(lecturerIds);

        Thesis t = new Thesis();
        t.setTitle(title);
        t.setCreatedDate(LocalDate.now());
        t.setStatus(Status.PENDING);
        t.setFileKhoaluan(fileThesis != null ? fileThesis.trim() : null);
        t.setMajor(major);

        List<User> students = studentIds.stream()
                .map(id -> userService.getUserById(id))
                .filter(u -> u != null && u.getUserRole() == UserRole.STUDENT)
                .collect(Collectors.toList());

        List<User> lecturers = lecturerIds.stream()
                .map(id -> userService.getUserById(id))
                .filter(u -> u != null && u.getUserRole() == UserRole.LECTURER)
                .collect(Collectors.toList());

        t.setStudents(new HashSet<>(students));
        t.setSupervisors(new HashSet<>(lecturers));

        return thesisRepo.addThesis(t);
    }

    @Override
    public List<Map<String, Object>> getScorePerYear() {
        List<Object[]> raw = this.thesisRepo.getScorePerYear();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Object[] row : raw) {
            Map<String, Object> map = new HashMap<>();
            map.put("year", row[0]);
            map.put("avgScore", row[1]);
            result.add(map);
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> getThesisCountByMajor(int year) {
        List<Object[]> raw = this.thesisRepo.getThesisCountByMajorInYear(year);
        List<Map<String, Object>> result = new ArrayList<>();

        for (Object[] row : raw) {
            Map<String, Object> map = new HashMap<>();
            map.put("major", row[0].toString());
            map.put("count", row[1]);
            result.add(map);
        }

        return result;
    }

    @Override
    public boolean submitThesisFile(String username, String file) {
        try {
            User me = userService.getUserByUsername(username);
            if (file == null || file.isBlank()) {
                return false;
            }
            Thesis t = thesisRepo.getThesisByStudentId(me.getId()); // hàm repo ở dưới
            if (t == null) {
                return false;
            }

            // (tuỳ quy định) chặn nộp khi đã kết thúc
            if (t.getStatus() == Status.CLOSED || t.getStatus() == Status.DEFENDED) {
                return false;
            }

            t.setFileKhoaluan(file);
            t.setStatus(Status.DEFENDED);

            return thesisRepo.updateThesis(t);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
