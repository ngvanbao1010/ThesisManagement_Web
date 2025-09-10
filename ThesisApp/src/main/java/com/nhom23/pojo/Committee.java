/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.nhom23.pojo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;


/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "committee")
public class Committee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "defense_date")
    private LocalDateTime defenseDate;

    private Boolean available;

    @OneToMany(mappedBy = "committee", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<CommitteeMember> members;
    
    @OneToMany(mappedBy = "committee", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Thesis> theses;

    // Getters and Setters

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the defenseDate
     */
    public LocalDateTime getDefenseDate() {
        return defenseDate;
    }

    /**
     * @param defenseDate the defenseDate to set
     */
    public void setDefenseDate(LocalDateTime defenseDate) {
        this.defenseDate = defenseDate;
    }

    /**
     * @return the available
     */
    public Boolean getAvailable() {
        return available;
    }

    /**
     * @param available the available to set
     */
    public void setAvailable(Boolean available) {
        this.available = available;
    }

    /**
     * @return the members
     */
    public Set<CommitteeMember> getMembers() {
        return members;
    }

    /**
     * @param members the members to set
     */
    public void setMembers(Set<CommitteeMember> members) {
        this.members = members;
    }

    /**
     * @return the theses
     */
    public Set<Thesis> getTheses() {
        return theses;
    }

    /**
     * @param theses the theses to set
     */
    public void setTheses(Set<Thesis> theses) {
        this.theses = theses;
    }
}
