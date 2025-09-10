/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nhom23.services;

import com.nhom23.pojo.CommitteeMember;
import com.nhom23.pojo.CommitteeMemberId;
import java.util.List;

public interface CommitteeMemberService {
    List<CommitteeMember> getMembersByCommitteeId(int committeeId);
    boolean addOrUpdateMember(CommitteeMember member);
    boolean deleteMember(CommitteeMemberId id);
    boolean isMemberOfThesisCommittee(int lecturerId, int thesisId);

}

