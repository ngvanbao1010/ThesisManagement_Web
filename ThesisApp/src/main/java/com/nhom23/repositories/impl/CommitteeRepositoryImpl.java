/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom23.repositories.impl;

import com.nhom23.pojo.Committee;
import com.nhom23.pojo.Status;
import com.nhom23.pojo.Thesis;
import com.nhom23.repositories.CommitteeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class CommitteeRepositoryImpl implements CommitteeRepository {

    @Autowired
    private LocalSessionFactoryBean factory;
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Committee> getCommittees() {
        Session s = this.factory.getObject().getCurrentSession();
        return s.createQuery("FROM Committee", Committee.class).getResultList();
    }

    @Override
    public Committee getCommitteeById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(Committee.class, id);
    }

    @Override
    public boolean addCommittee(Committee c) {
        Session session = factory.getObject().getCurrentSession();
        try {
            session.save(c);
            session.flush();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean updateCommittee(Committee c) {
        try {
            Session s = this.factory.getObject().getCurrentSession();
            s.update(c);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean deleteCommittee(int id) {
        try {
            Session s = this.factory.getObject().getCurrentSession();
            Committee c = s.get(Committee.class, id);
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
    public boolean lockCommittee(int committeeId) {
        Session session = this.factory.getObject().getCurrentSession();
        Committee c = session.get(Committee.class, committeeId);
        if (c == null) {
            return false;
        }

        c.setAvailable(false);

        for (Thesis t : c.getTheses()) {
            t.setStatus(Status.CLOSED); 
        }

        return true;
    }
    @Override
    public List<Thesis> getThesisById(int idCommittee) {
        String jpql = "SELECT t FROM Thesis t WHERE t.committee.id = :idCommittee";
        return entityManager.createQuery(jpql, Thesis.class)
                            .setParameter("idCommittee", idCommittee)
                            .getResultList();
    }
}
