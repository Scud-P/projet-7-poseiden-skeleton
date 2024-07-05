package com.nnk.springboot.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rating")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "moodysRating", length = 125)
    @NotBlank(message = "moodysRating is mandatory")
    @Size(max = 125, message = "Only 125 characters allowed")
    private String moodysRating;

    @Column(name = "sandPRating", length = 125)
    @NotBlank(message = "sandPRating is mandatory")
    @Size(max = 125, message = "Only 125 characters allowed")
    private String sandPRating;

    @Column(name = "fitchRating", length = 125)
    @NotBlank(message = "fitchRating is mandatory")
    @Size(max = 125, message = "Only 125 characters allowed")
    private String fitchRating;

    @NotNull(message = "orderNumber is mandatory")
    @Column(name = "orderNumber")
    @Min(value = 0, message = "Only positive values from 0 to 255")
    @Max(value = 255, message = "Only positive values from 0 to 255")
    private Integer orderNumber;
}
