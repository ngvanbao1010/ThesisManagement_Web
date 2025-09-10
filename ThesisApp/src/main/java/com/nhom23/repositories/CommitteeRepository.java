/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nhom23.repositories;

import com.nhom23.pojo.Committee;
import com.nhom23.pojo.Thesis;
import java.util.List;

public interface CommitteeRepository {
    List<Committee> getCommittees();           
    Committee getCommitteeById(int id);        
    boolean addCommittee(Committee c);      
    boolean updateCommittee(Committee c);   
    boolean deleteCommittee(int id);        
    boolean lockCommittee(int id);       
    List<Thesis> getThesisById(int idCommittee);
}
