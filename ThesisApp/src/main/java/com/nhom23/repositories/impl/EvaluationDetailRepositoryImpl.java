/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.nhom23.repositories.impl;

import com.nhom23.pojo.EvaluationDetail;
import com.nhom23.repositories.EvaluationDetailRepository;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class EvaluationDetailRepositoryImpl implements EvaluationDetailRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<EvaluationDetail> findByThesisId(int thesisId) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "FROM EvaluationDetail d WHERE d.thesis.id = :thesisId";
        return s.createQuery(hql, EvaluationDetail.class)
                .setParameter("thesisId", thesisId)
                .getResultList();
    }

    @Override
    public List<EvaluationDetail> getDetailsByLecturerId(int lecturerId) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "FROM EvaluationDetail d WHERE d.lecturer.id = :lecturerId";
        return s.createQuery(hql, EvaluationDetail.class)
                .setParameter("lecturerId", lecturerId)
                .getResultList();
    }

    @Override
    public boolean addOrUpdate(EvaluationDetail d) {
        try {
            Session s = this.factory.getObject().getCurrentSession();
            if (d.getId() == null) {
                s.persist(d);
            } else {
                s.update(d);
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        try {
            Session s = this.factory.getObject().getCurrentSession();
            EvaluationDetail d = s.get(EvaluationDetail.class, id);
            if (d != null) {
                s.delete(d);
                return true;
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public EvaluationDetail getDetailById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(EvaluationDetail.class, id);
    }

    @Override
    public boolean exists(int thesisId, int lecturerId, int criterionId) {
        try {
            Session session = factory.getObject().getCurrentSession();

            String hql = "SELECT COUNT(ed) FROM EvaluationDetail ed "
                    + "WHERE ed.thesis.id = :thesisId AND ed.lecturer.id = :lecturerId AND ed.criterion.id = :criterionId";

            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("thesisId", thesisId);
            query.setParameter("lecturerId", lecturerId);
            query.setParameter("criterionId", criterionId);

            Long count = query.uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            System.err.println("Lỗi khi kiểm tra tồn tại EvaluationDetail: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public double getScoreOfThesis(int thesisId) {
        Session session = this.factory.getObject().getCurrentSession();

        String hql = """
        SELECT AVG(lecturerAvg) FROM (
            SELECT AVG(ed.score) AS lecturerAvg
            FROM EvaluationDetail ed
            WHERE ed.thesis.id = :thesisId
            GROUP BY ed.lecturer.id
        )
    """;

        String hql2 = """
        SELECT ed.lecturer.id, AVG(ed.score)
        FROM EvaluationDetail ed
        WHERE ed.thesis.id = :thesisId
        GROUP BY ed.lecturer.id
    """;

        List<Object[]> lecturerAverages = session.createQuery(hql2).setParameter("thesisId", thesisId).getResultList();

        if (lecturerAverages.isEmpty()) {
            return 0;
        }

        double total = 0;
        for (Object[] row : lecturerAverages) {
            total += (double) row[1];
        }

        return total / lecturerAverages.size(); 
    }
}
