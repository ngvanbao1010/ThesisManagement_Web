/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nhom23.services;

import com.nhom23.pojo.Committee;
import java.time.LocalDateTime;
import java.util.List;

public interface CommitteeService {

    boolean createCommittee(String name, LocalDateTime defenseDate, List<Integer> memberIds, List<Integer> thesisIds);

    List<Committee> getCommittees();               

    Committee getCommitteeById(int id);            

    boolean addCommittee(Committee c);            

    boolean updateCommittee(Committee c);        

    boolean deleteCommittee(int id);              

    boolean lockCommittee(int id);             

}
