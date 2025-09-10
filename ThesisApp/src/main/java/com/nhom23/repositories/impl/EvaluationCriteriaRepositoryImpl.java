/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom23.repositories.impl;

import com.nhom23.pojo.EvaluationCriteria;
import com.nhom23.repositories.EvaluationCriteriaRepository;
import java.util.Collections;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class EvaluationCriteriaRepositoryImpl implements EvaluationCriteriaRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<EvaluationCriteria> getAllCriteria() {
        Session s = this.factory.getObject().getCurrentSession();
        return s.createQuery("FROM EvaluationCriteria", EvaluationCriteria.class).getResultList();
    }

    @Override
    public EvaluationCriteria getCriterionById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(EvaluationCriteria.class, id);
    }

    @Override
    public boolean addCriterion(EvaluationCriteria c) {
        try {
            Session s = this.factory.getObject().getCurrentSession();
            s.persist(c);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean deleteCriterion(int id) {
        try {
            Session s = this.factory.getObject().getCurrentSession();
            EvaluationCriteria c = s.get(EvaluationCriteria.class, id);
            if (c != null) {
                s.delete(c);
                return true;
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }
    @Override
    public List<EvaluationCriteria> getCriteriaByThesis(int thesisId) {
        try {
            Session session = factory.getObject().getCurrentSession();

            String hql = "SELECT DISTINCT ed.criterion FROM EvaluationDetail ed WHERE ed.thesis.id = :thesisId";

            Query<EvaluationCriteria> query = session.createQuery(hql, EvaluationCriteria.class);
            query.setParameter("thesisId", thesisId);

            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy tiêu chí theo khóa luận: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
