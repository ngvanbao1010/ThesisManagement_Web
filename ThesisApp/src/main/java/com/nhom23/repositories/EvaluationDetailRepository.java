/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.nhom23.repositories;

import com.nhom23.pojo.EvaluationDetail;
import java.util.List;

public interface EvaluationDetailRepository {

    List<EvaluationDetail> findByThesisId(int thesisId);        

    List<EvaluationDetail> getDetailsByLecturerId(int lecturerId);   

    boolean addOrUpdate(EvaluationDetail d);                        

    boolean delete(int id);                                       

    EvaluationDetail getDetailById(int id);                        

    boolean exists(int thesisId, int lecturerId, int criterionId);

    double getScoreOfThesis(int thesisId);

}
