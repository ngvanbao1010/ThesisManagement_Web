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
@Table(name = "evaluation_detail")
public class EvaluationDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "thesis_id")
    private Thesis thesis;

    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private User lecturer;

    @ManyToOne
    @JoinColumn(name = "criterion_id")
    private EvaluationCriteria criterion;

    private Double score;

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
     * @return the thesis
     */
    public Thesis getThesis() {
        return thesis;
    }

    /**
     * @param thesis the thesis to set
     */
    public void setThesis(Thesis thesis) {
        this.thesis = thesis;
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
     * @return the criterion
     */
    public EvaluationCriteria getCriterion() {
        return criterion;
    }

    /**
     * @param criterion the criterion to set
     */
    public void setCriterion(EvaluationCriteria criterion) {
        this.criterion = criterion;
    }

    /**
     * @return the score
     */
    public Double getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(Double score) {
        this.score = score;
    }
}
