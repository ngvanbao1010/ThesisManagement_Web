/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nhom23.services;

import com.nhom23.pojo.Major;
import com.nhom23.pojo.Thesis;
import com.nhom23.pojo.User;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

public interface ThesisService {

    List<Thesis> getAllTheses();

    Thesis getThesesByStudentId(int studentId);

    List<Thesis> getThesesByLecturerId(int lecturerId);

    Thesis getThesisById(int id);

    boolean addThesis(Thesis t);

    boolean updateThesis(Thesis t);

    boolean deleteThesis(int id); 

    public boolean addThesisByStaff(String title, List<Integer> lecturerIds, List<Integer> studentIds, String fileThesis, Major major);

    List<Map<String, Object>> getScorePerYear();

    List<Map<String, Object>> getThesisCountByMajor(int year);
    
    boolean submitThesisFile(String username, String file);

}
