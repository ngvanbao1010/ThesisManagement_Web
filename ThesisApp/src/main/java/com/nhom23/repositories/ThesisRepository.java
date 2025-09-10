/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nhom23.repositories;

import com.nhom23.pojo.Thesis;
import java.util.List;
import java.util.Set;

public interface ThesisRepository {

    List<Thesis> getTheses();

    Thesis getThesisById(int id);

    boolean addThesis(Thesis t);

    boolean updateThesis(Thesis t);

    Thesis getThesisByStudentId(int studentId);

    List<Thesis> getThesesByLecturerId(int lecturerId);

    void updateStatus(int thesisId);

    boolean deleteThesis(int id);

    List<Object[]> getScorePerYear();

    List<Object[]> getThesisCountByMajorInYear(int year);

    List<Object[]> getThesisScoreSheet(int thesisId);
    
    Set<Integer> getSupervisorIdsOfTheses(List<Integer> thesisIds);


}
