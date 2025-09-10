/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nhom23.repositories;

import com.nhom23.pojo.EvaluationCriteria;
import java.util.List;

public interface EvaluationCriteriaRepository {
    List<EvaluationCriteria> getAllCriteria();    
    EvaluationCriteria getCriterionById(int id);    
    boolean addCriterion(EvaluationCriteria c);      
    boolean deleteCriterion(int id);                
    List<EvaluationCriteria> getCriteriaByThesis(int thesisId);     
}

