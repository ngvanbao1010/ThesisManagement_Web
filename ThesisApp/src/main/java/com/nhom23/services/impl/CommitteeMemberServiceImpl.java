/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom23.services.impl;

import com.nhom23.pojo.CommitteeMember;
import com.nhom23.pojo.CommitteeMemberId;
import com.nhom23.repositories.CommitteeMemberRepository;
import com.nhom23.services.CommitteeMemberService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommitteeMemberServiceImpl implements CommitteeMemberService {

    @Autowired
    private CommitteeMemberRepository cmRepo;

    @Override
    public List<CommitteeMember> getMembersByCommitteeId(int committeeId) {
        return this.cmRepo.getMembersByCommitteeId(committeeId);
    }

    @Override
    public boolean addOrUpdateMember(CommitteeMember member) {
        return this.cmRepo.addOrUpdateMember(member);
    }

    @Override
    public boolean deleteMember(CommitteeMemberId id) {
        return this.cmRepo.deleteMember(id);
    }

    @Override
    public boolean isMemberOfThesisCommittee(int lecturerId, int thesisId) {
        return cmRepo.isMemberOfThesisCommittee(lecturerId, thesisId);
    }
}
