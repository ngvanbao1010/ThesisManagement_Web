package com.nhom23.repositories.impl;

import com.nhom23.pojo.User;
import com.nhom23.pojo.UserRole;
import com.nhom23.repositories.UserRepository;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User getUserByUsername(String username) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("User.findByUsername", User.class);
        q.setParameter("username", username);

        try {
            return (User) q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public User getUserById(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        return s.get(User.class, id);
    }

    @Override
    public List<User> getUsers() {
        Session s = this.factory.getObject().getCurrentSession();
        return s.createQuery("FROM User", User.class).getResultList();
    }

    @Override
    public User addUser(User u) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(u);
        return u;
    }

    @Override
    public boolean updateUser(User u) {
        if (u == null || u.getId() == null) {
            return false;
        }

        try {
            Session s = this.factory.getObject().getCurrentSession();
            s.merge(u);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean changePassword(int userId, String encodedNewPass) {
        try {
            Session session = this.factory.getObject().getCurrentSession();
            User u = session.get(User.class, userId);

            if (u == null) {
                return false;
            }

            u.setPassword(encodedNewPass);
            session.merge(u);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public List<User> getUsersByRole(UserRole role) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = "FROM User u WHERE u.userRole = :role";
        return s.createQuery(hql, User.class)
                .setParameter("role", role)
                .getResultList();
    }

    @Override
    public List<User> getLecturersNotSupervisorsOfTheses(List<Integer> thesisIds) {
        Session s = this.factory.getObject().getCurrentSession();
        String hql = """
        select distinct u
        from User u
        where u.userRole = :role
        and u.id not in (
            select sup.id
            from Thesis t
            join t.supervisors sup
            where t.id in (:ids)
        )
    """;
        return s.createQuery(hql, User.class)
                .setParameter("role", UserRole.LECTURER)
                .setParameterList("ids", thesisIds)
                .getResultList();
    }
}
