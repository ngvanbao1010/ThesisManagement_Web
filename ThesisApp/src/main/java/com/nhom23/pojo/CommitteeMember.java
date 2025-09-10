/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom23.pojo;
import jakarta.persistence.*;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "committee_members")
public class CommitteeMember {
    @EmbeddedId
    private CommitteeMemberId id;

    @ManyToOne
    @MapsId("committeeId")
    @JoinColumn(name = "committee_id")
    private Committee committee;

    @ManyToOne
    @MapsId("lecturerId")
    @JoinColumn(name = "lecturer_id")
    private User lecturer;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        CHAIRMAN,
        SECRETARY,
        REVIEWER,
        MEMBER
    }

    // Getters and Setters

    /**
     * @return the id
     */
    public CommitteeMemberId getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(CommitteeMemberId id) {
        this.id = id;
    }

    /**
     * @return the committee
     */
    public Committee getCommittee() {
        return committee;
    }

    /**
     * @param committee the committee to set
     */
    public void setCommittee(Committee committee) {
        this.committee = committee;
    }

    /**
     * @return the lecturer
     */
    public User getLecturer() {
        return lecturer;
    }

    /**
     * @param lecturer the lecturer to set
     */
    public void setLecturer(User lecturer) {
        this.lecturer = lecturer;
    }

    /**
     * @return the role
     */
    public Role getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(Role role) {
        this.role = role;
    }
}
