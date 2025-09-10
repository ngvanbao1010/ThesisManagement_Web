package com.nhom23.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "thesis")
public class Thesis implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToMany
    @JoinTable(
            name = "thesis_students",
            joinColumns = @JoinColumn(name = "thesis_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    @JsonIgnore
    private Set<User> students;

    @ManyToMany
    @JoinTable(
            name = "thesis_lecturers",
            joinColumns = @JoinColumn(name = "thesis_id"),
            inverseJoinColumns = @JoinColumn(name = "lecturer_id")
    )
    @JsonIgnore
    private Set<User> supervisors;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDate createdDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "committee_id")
    private Committee committee;

    @Enumerated(EnumType.STRING)
    @Column(name = "major")
    private Major major;

    @Column(name = "file_khoaluan")
    private String fileKhoaluan; 

    // Getter - Setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<User> getStudents() {
        return students;
    }

    public void setStudents(Set<User> students) {
        this.students = students;
    }

    public Set<User> getSupervisors() {
        return supervisors;
    }

    public void setSupervisors(Set<User> supervisors) {
        this.supervisors = supervisors;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * @return the fileKhoaluan
     */
    public String getFileKhoaluan() {
        return fileKhoaluan;
    }

    /**
     * @param fileKhoaluan the fileKhoaluan to set
     */
    public void setFileKhoaluan(String fileKhoaluan) {
        this.fileKhoaluan = fileKhoaluan;
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
     * @return the major
     */
    public Major getMajor() {
        return major;
    }

    /**
     * @param major the major to set
     */
    public void setMajor(Major major) {
        this.major = major;
    }
}
