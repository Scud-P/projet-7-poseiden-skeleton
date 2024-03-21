package com.nnk.springboot.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "rating")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="moodysRating")
    private String moodysRating;

    @Column(name="sandPRating")
    private String sandPRating;

    @Column(name="fitchRating")
    private String fitchRating;

    @Column(name="orderNumber")
    private int orderNumber;

}
