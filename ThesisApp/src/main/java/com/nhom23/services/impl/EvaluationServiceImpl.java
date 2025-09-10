/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.nhom23.services.impl;

import com.nhom23.pojo.EvaluationCriteria;
import com.nhom23.pojo.EvaluationDetail;
import com.nhom23.repositories.EvaluationCriteriaRepository;
import com.nhom23.repositories.EvaluationDetailRepository;
import com.nhom23.repositories.ThesisRepository;
import com.nhom23.repositories.UserRepository;
import com.nhom23.services.EvaluationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Administrator
 */
@Service
@Transactional
public class EvaluationServiceImpl implements EvaluationService {

    @Autowired
    private EvaluationCriteriaRepository criteriaRepo;

    @Autowired
    private EvaluationDetailRepository detailRepo;

    @Autowired
    private ThesisRepository thesisRepo;

    @Autowired
    private UserRepository userRepo;

    @Override
    public EvaluationCriteria addCriterion(String criterion) {
        EvaluationCriteria ec = new EvaluationCriteria();
        ec.setCriterion(criterion);
        criteriaRepo.addCriterion(ec);
        return ec;
    }

    @Override
    public List<EvaluationCriteria> getAllCriteria() {
        return criteriaRepo.getAllCriteria();
    }

    @Override
    public EvaluationDetail addEvaluationDetail(int thesisId, int lecturerId, int criterionId, double score) {
        EvaluationDetail ed = new EvaluationDetail();
        ed.setThesis(thesisRepo.getThesisById(thesisId));
        ed.setLecturer(userRepo.getUserById(lecturerId));
        ed.setCriterion(criteriaRepo.getCriterionById(criterionId));
        ed.setScore(score);
        detailRepo.addOrUpdate(ed);
        return ed;
    }

    @Override
    public List<EvaluationDetail> getEvaluationDetailsForThesis(int thesisId) {
        return detailRepo.findByThesisId(thesisId);
    }
}
