/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.nhom23.services;

import com.nhom23.pojo.EvaluationCriteria;
import com.nhom23.pojo.EvaluationDetail;
import java.util.List;

/**
 *
 * @author Administrator
 */
public interface EvaluationService {

    EvaluationCriteria addCriterion(String criterion);
    List<EvaluationCriteria> getAllCriteria();

    EvaluationDetail addEvaluationDetail(int thesisId, int lecturerId, int criterionId, double score);
    List<EvaluationDetail> getEvaluationDetailsForThesis(int thesisId);
    
}
