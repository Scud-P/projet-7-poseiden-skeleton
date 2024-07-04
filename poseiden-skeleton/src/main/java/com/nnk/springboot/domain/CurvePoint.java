package com.nnk.springboot.domain;

import jakarta.validation.constraints.Digits;
import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "curvepoint")
public class CurvePoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull(message = "curveId is mandatory")
    @Column(name = "curveId")
    private Integer curveId;

    @Column(name = "asOfDate")
    private Timestamp asOfDate;

//    @Digits(integer = 10, fraction = 2, message = "Only numerical values with up to two decimals and a dot separator are allowed for this field (example: 11.23).")
    @Column(name = "term")
    private Double term;

//    @Digits(integer = 10, fraction = 2, message = "Only numerical values with up to two decimals and a dot separator are allowed for this field (example: 11.23).")
    @Column(name = "value")
    private Double value;

    @Column(name = "creationDate")
    private Timestamp creationDate;

}
