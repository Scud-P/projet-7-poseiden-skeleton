package com.nnk.springboot.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "rating")
public class Rating {

    public Rating(int id, String moodysRating, String sandPRating, String fitchRating, int orderNumber) {
        this.id = id;
        this.moodysRating = moodysRating;
        this.sandPRating = sandPRating;
        this.fitchRating = fitchRating;
        this.orderNumber = orderNumber;
    }

    public Rating() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "moodysRating")
    @NotNull
    @NotBlank(message = "moodysRating is mandatory")
    private String moodysRating;

    @Column(name = "sandPRating")
    @NotNull
    @NotBlank(message = "sandPRating is mandatory")
    private String sandPRating;

    @Column(name = "fitchRating")
    @NotNull
    @NotBlank(message = "fitchRating is mandatory")
    private String fitchRating;

    @Column(name = "orderNumber")
    @NotNull(message = "orderNumber is mandatory")
    private Integer orderNumber;

}
