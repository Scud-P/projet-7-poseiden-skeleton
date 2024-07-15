package com.nnk.springboot.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rulename")
public class RuleName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotBlank(message = "name is mandatory")
    @Size(max = 125, message = "Can't be more than 125 characters.")
    @Column(name = "name", length = 125)
    private String name;

    @NotBlank(message = "description is mandatory")
    @Size(max = 125, message = "Can't be more than 125 characters.")
    @Column(name = "description", length = 125)
    private String description;

    @NotBlank(message = "Json is mandatory")
    @Size(max = 125, message = "Can't be more than 125 characters.")
    @Column(name = "json", length = 125)
    private String json;

    @Column(name = "template", length = 512)
    @Size(max = 512, message = "Can't be more than 512 characters.")
    private String template;

    @Column(name = "sqlStr", length = 125)
    @Size(max = 125, message = "Can't be more than 125 characters.")
    private String sqlStr;

    @Column(name = "sqlPart", length = 125)
    @Size(max = 125, message = "Can't be more than 125 characters.")
    private String sqlPart;
}
