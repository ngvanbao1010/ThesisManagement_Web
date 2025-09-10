/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package com.nhom23.pojo;
import jakarta.persistence.*;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "evaluation_criteria")
public class EvaluationCriteria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String criterion;

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
     * @return the criterion
     */
    public String getCriterion() {
        return criterion;
    }

    /**
     * @param criterion the criterion to set
     */
    public void setCriterion(String criterion) {
        this.criterion = criterion;
    }
}
