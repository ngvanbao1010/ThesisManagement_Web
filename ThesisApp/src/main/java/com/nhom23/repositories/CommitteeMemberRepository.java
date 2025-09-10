/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.nhom23.repositories;

import com.nhom23.pojo.CommitteeMember;
import com.nhom23.pojo.CommitteeMemberId;
import java.util.List;

public interface CommitteeMemberRepository {
    List<CommitteeMember> getMembersByCommitteeId(int committeeId);
    boolean addOrUpdateMember(CommitteeMember cm);
    boolean deleteMember(CommitteeMemberId id);
    CommitteeMember getMember(CommitteeMemberId id);
    boolean isReviewerOfThesis(int userId, int thesisId);
    boolean isMemberOfThesisCommittee(int lecturerId, int thesisId);
}
