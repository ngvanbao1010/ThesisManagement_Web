/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nhom23.pojo;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Administrator
 */
@Embeddable
public class CommitteeMemberId implements Serializable {

    private Integer committeeId;
    private Integer lecturerId;

    public CommitteeMemberId() {
    }

    public CommitteeMemberId(Integer committeeId, Integer lecturerId) {
        this.committeeId = committeeId;
        this.lecturerId = lecturerId;
    }

    // equals() and hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CommitteeMemberId that = (CommitteeMemberId) o;
        return Objects.equals(getCommitteeId(), that.getCommitteeId())
                && Objects.equals(getLecturerId(), that.getLecturerId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommitteeId(), getLecturerId());
    }

    /**
     * @return the committeeId
     */
    public Integer getCommitteeId() {
        return committeeId;
    }

    /**
     * @param committeeId the committeeId to set
     */
    public void setCommitteeId(Integer committeeId) {
        this.committeeId = committeeId;
    }

    /**
     * @return the lecturerId
     */
    public Integer getLecturerId() {
        return lecturerId;
    }

    /**
     * @param lecturerId the lecturerId to set
     */
    public void setLecturerId(Integer lecturerId) {
        this.lecturerId = lecturerId;
    }

}
