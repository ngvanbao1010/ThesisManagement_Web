/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom23.repositories.impl;

import com.nhom23.pojo.Status;
import com.nhom23.pojo.Thesis;
import com.nhom23.repositories.ThesisRepository;
import java.util.List;
import java.util.Set;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ThesisRepositoryImpl implements ThesisRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Thesis> getTheses() {
        Session s = this.factory.getObject().getCurrentSession();
        return s.createQuery("FROM Thesis", Thesis.class).getResultList();
    }

    @Override
    public boolean addThesis(Thesis t) {
        try {
            Session s = this.factory.getObject().getCurrentSession();
            s.persist(t);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean updateThesis(Thesis t) {
        try {
            Session s = this.factory.getObject().getCurrentSession();
            s.update(t);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public Thesis getThesisById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Thesis.class, id);
    }

    @Override
    public Thesis getThesisByStudentId(int studentId) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "SELECT t FROM Thesis t JOIN t.students s WHERE s.id = :studentId";
        return s.createQuery(hql, Thesis.class)
                .setParameter("studentId", studentId)
                .setMaxResults(1)
                .uniqueResult();
    }

    @Override
    public List<Thesis> getThesesByLecturerId(int lecturerId) {
        Session session = this.factory.getObject().getCurrentSession();
        String hql = "SELECT t FROM Thesis t JOIN t.supervisors s WHERE s.id = :lecturerId";
        return session.createQuery(hql, Thesis.class)
                .setParameter("lecturerId", lecturerId)
                .getResultList();
    }

    @Override
    public boolean deleteThesis(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        Thesis t = session.get(Thesis.class, id);
        if (t != null) {
            session.delete(t);
            return true;
        }
        return false;
    }

    @Override
    public void updateStatus(int thesisId) {
        Session s = this.factory.getObject().getCurrentSession();
        Thesis thesis = s.get(Thesis.class, thesisId);
        thesis.setStatus(Status.DEFENDED);
    }

    @Override
    public List<Object[]> getScorePerYear() {
        Session session = this.factory.getObject().getCurrentSession();
        String sql = """
        SELECT YEAR(t.created_date) AS year, ROUND(AVG(e.score), 2) AS avg_score
        FROM thesis t
        JOIN evaluation_detail e ON e.thesis_id = t.id
        WHERE t.status = 'CLOSED'
        GROUP BY YEAR(t.created_date)
        ORDER BY YEAR(t.created_date)
    """;
        Query query = session.createNativeQuery(sql);
        return query.getResultList();
    }

    @Override
    public List<Object[]> getThesisCountByMajorInYear(int year) {
        Session session = this.factory.getObject().getCurrentSession();
        String sql = """
        SELECT t.major, COUNT(t.id) AS count
        FROM thesis t
        WHERE YEAR(t.created_date) = :year AND t.status = 'CLOSED'
        GROUP BY t.major
        ORDER BY t.major
    """;
        Query query = session.createNativeQuery(sql);
        query.setParameter("year", year);
        return query.getResultList();
    }

    @Override
    public List<Object[]> getThesisScoreSheet(int thesisId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query q = session.createNativeQuery("""
        SELECT 
            t.id AS thesis_id,
            t.title AS thesis_title,
            t.created_date,
            t.file_khoaluan,
            
            CONCAT(u.last_name, ' ', u.first_name) AS student_name,
            u.username AS student_code,
            u.email,
            u.phone,
            t.major AS major,
        
            CONCAT(l.last_name, ' ', l.first_name) AS lecturer_name,
            ec.criterion AS criterion_name,
            ed.score
            
        FROM thesis t
        JOIN thesis_students ts ON t.id = ts.thesis_id
        JOIN user u ON ts.student_id = u.id
        
        JOIN evaluation_detail ed ON t.id = ed.thesis_id
        JOIN evaluation_criteria ec ON ed.criterion_id = ec.id
        JOIN user l ON ed.lecturer_id = l.id
        
        WHERE t.id = :thesisId AND t.status = 'CLOSED'
        ORDER BY student_name, lecturer_name, ec.id;
    """);

        q.setParameter("thesisId", thesisId);
        return q.getResultList();
    }

    @Override
    public Set<Integer> getSupervisorIdsOfTheses(List<Integer> thesisIds) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = """
        select distinct sup.id
        from Thesis t
        join t.supervisors sup
        where t.id in (:ids)
    """;
        List<Integer> res = s.createQuery(hql, Integer.class)
                .setParameterList("ids", thesisIds)
                .getResultList();
        return new java.util.HashSet<>(res);
    }

}
