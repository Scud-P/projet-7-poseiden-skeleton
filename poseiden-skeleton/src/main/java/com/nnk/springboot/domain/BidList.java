package com.nnk.springboot.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "bidlist")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BidList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;


    @NotBlank(message = "Account is mandatory")
    @Size(max = 30, message = "Account must be at most 30 characters")
    @Column(name = "account")
    private String account;

    @NotBlank(message = "Type is mandatory")
    @Size(max = 30, message = "Type must be at most 30 characters")
    @Column(name = "type")
    private String type;

    @Column(name = "bidQuantity")
    private Double bidQuantity;

    @Column(name = "askQuantity")
    private double askQuantity;
    @Column(name = "bid")
    private double bid;
    @Column(name = "ask")
    private double ask;
    @Column(name = "benchmark")
    private String benchmark;
    @Column(name = "bidListDate")
    private Timestamp bidListDate;
    @Column(name = "commentary")
    private String commentary;
    @Column(name = "security")
    private String security;
    @Column(name = "status")
    private String status;
    @Column(name = "trader")
    private String trader;
    @Column(name = "book")
    private String book;
    @Column(name = "creationName")
    private String creationName;
    @Column(name = "creationDate")
    private Timestamp creationDate;
    @Column(name = "revisionName")
    private String revisionName;
    @Column(name = "revisionDate")
    private Timestamp revisionDate;
    @Column(name = "dealName")
    private String dealName;
    @Column(name = "dealType")
    private String dealType;
    @Column(name = "sourceListId")
    private String sourceListId;
    @Column(name = "side")
    private String side;

}
