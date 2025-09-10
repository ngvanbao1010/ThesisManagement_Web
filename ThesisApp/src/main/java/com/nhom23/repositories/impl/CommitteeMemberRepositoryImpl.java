/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom23.repositories.impl;

import com.nhom23.pojo.CommitteeMember;
import com.nhom23.pojo.CommitteeMemberId;
import com.nhom23.repositories.CommitteeMemberRepository;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class CommitteeMemberRepositoryImpl implements CommitteeMemberRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<CommitteeMember> getMembersByCommitteeId(int committeeId) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "FROM CommitteeMember cm WHERE cm.committee.id = :committeeId";
        return s.createQuery(hql, CommitteeMember.class)
                .setParameter("committeeId", committeeId)
                .getResultList();
    }

    @Override
    public boolean addOrUpdateMember(CommitteeMember cm) {
        try {
            Session s = this.factory.getObject().getCurrentSession();
            s.persist(cm);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean deleteMember(CommitteeMemberId id) {
        try {
            Session s = this.factory.getObject().getCurrentSession();
            CommitteeMember cm = s.get(CommitteeMember.class, id);
            if (cm != null) {
                s.remove(cm);
                return true;
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public CommitteeMember getMember(CommitteeMemberId id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(CommitteeMember.class, id);
    }

    @Override
    public boolean isReviewerOfThesis(int userId, int thesisId) {
        try {
            Session session = factory.getObject().getCurrentSession();

            String hql = "SELECT COUNT(cm) FROM CommitteeMember cm "
                    + "JOIN cm.committee c "
                    + "JOIN c.theses t "
                    + "WHERE cm.lecturer.id = :userId AND t.id = :thesisId AND cm.role = 'REVIEWER'";

            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("userId", userId);
            query.setParameter("thesisId", thesisId);

            Long count = query.uniqueResult();
            return count != null && count > 0;

        } catch (Exception e) {
            System.err.println("Lỗi khi kiểm tra reviewer: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isMemberOfThesisCommittee(int lecturerId, int thesisId) {
        try {
            Session session = factory.getObject().getCurrentSession();

            String hql = "SELECT COUNT(cm) FROM CommitteeMember cm "
                    + "JOIN cm.committee c "
                    + "JOIN c.theses t "
                    + "WHERE cm.lecturer.id = :lecturerId AND t.id = :thesisId";

            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("lecturerId", lecturerId);
            query.setParameter("thesisId", thesisId);

            Long count = query.uniqueResult();
            return count != null && count > 0;

        } catch (Exception e) {
            System.err.println("Lỗi khi kiểm tra thành viên hội đồng: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
