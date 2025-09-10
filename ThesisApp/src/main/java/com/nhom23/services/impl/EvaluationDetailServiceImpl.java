/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom23.services.impl;

import com.nhom23.pojo.EvaluationDetail;
import com.nhom23.repositories.EvaluationDetailRepository;
import com.nhom23.services.EvaluationDetailService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EvaluationDetailServiceImpl implements EvaluationDetailService {

    @Autowired
    private EvaluationDetailRepository evalDetailRepo;

    @Override
    public List<EvaluationDetail> getDetailsByThesisId(int thesisId) {
        return this.evalDetailRepo.findByThesisId(thesisId);
    }

    @Override
    public List<EvaluationDetail> getDetailsByLecturerId(int lecturerId) {
        return this.evalDetailRepo.getDetailsByLecturerId(lecturerId);
    }

    @Override
    public boolean addOrUpdate(EvaluationDetail d) {

        return this.evalDetailRepo.addOrUpdate(d);
    }

    @Override
    public boolean delete(int id) {
        return this.evalDetailRepo.delete(id);
    }

    @Override
    public EvaluationDetail getDetailById(int id) {
        return this.evalDetailRepo.getDetailById(id);
    }

    @Override
    public boolean exists(int thesisId, int lecturerId, int criterionId) {
        return evalDetailRepo.exists(thesisId, lecturerId, criterionId);
    }

    @Override
    public double getScoreOfThesis(int thesisId) {
        return this.evalDetailRepo.getScoreOfThesis(thesisId);
    }
}
