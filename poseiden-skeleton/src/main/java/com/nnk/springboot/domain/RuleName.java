package com.nnk.springboot.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "rulename")
public class RuleName {
    public RuleName(int id, String name, String description, String json, String template, String sqlStr, String sqlPart) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.json = json;
        this.template = template;
        this.sqlStr = sqlStr;
        this.sqlPart = sqlPart;
    }

    public RuleName() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotBlank(message = "name is mandatory")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "name is mandatory")
    @Column(name = "description")
    private String description;

    @NotBlank(message = "name is mandatory")
    @Column(name = "json")
    private String json;

    @Column(name = "template")
    private String template;

    @Column(name = "sqlStr")
    private String sqlStr;

    @Column(name = "sqlPart")
    private String sqlPart;

}
