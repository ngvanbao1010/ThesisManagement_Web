package com.nhom23.repositories;

import com.nhom23.pojo.User;
import com.nhom23.pojo.UserRole;
import java.util.List;

public interface UserRepository {

    User getUserByUsername(String username);

    User addUser(User u);

    List<User> getUsers();

    boolean updateUser(User u);

    boolean changePassword(int userId, String encodedNewPass);

    User getUserById(int id);
    
    List<User> getUsersByRole(UserRole role);
    
    List<User> getLecturersNotSupervisorsOfTheses(List<Integer> thesisIds);

}
