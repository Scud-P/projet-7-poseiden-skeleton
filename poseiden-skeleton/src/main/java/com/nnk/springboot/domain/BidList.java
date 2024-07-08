package com.nnk.springboot.domain;

import com.nnk.springboot.domain.parameter.BidListParameter;
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
    @Column(name = "account", length = 30)
    private String account;

    @NotBlank(message = "Type is mandatory")
    @Size(max = 30, message = "Type must be at most 30 characters")
    @Column(name = "type", length = 30)
    private String type;

    @Column(name = "bidQuantity")
    private Double bidQuantity;

    @Column(name = "askQuantity")
    private double askQuantity;
    @Column(name = "bid")
    private double bid;
    @Column(name = "ask")
    private double ask;
    @Column(name = "benchmark", length = 125)
    @Size(max = 125, message = "Only 125 characters allowed")
    private String benchmark;
    @Column(name = "bidListDate")
    private Timestamp bidListDate;
    @Column(name = "commentary", length = 125)
    @Size(max = 125, message = "Only 125 characters allowed")
    private String commentary;
    @Column(name = "security", length = 125)
    @Size(max = 125, message = "Only 125 characters allowed")
    private String security;
    @Column(name = "status", length = 10)
    @Size(max = 10, message = "Only 125 characters allowed")
    private String status;
    @Column(name = "trader", length = 125)
    @Size(max = 125, message = "Only 125 characters allowed")
    private String trader;
    @Column(name = "book", length = 125)
    @Size(max = 125, message = "Only 125 characters allowed")
    private String book;
    @Column(name = "creationName", length = 125)
    @Size(max = 125, message = "Only 125 characters allowed")
    private String creationName;
    @Column(name = "creationDate")
    private Timestamp creationDate;
    @Column(name = "revisionName", length = 125)
    @Size(max = 125, message = "Only 125 characters allowed")
    private String revisionName;
    @Column(name = "revisionDate")
    private Timestamp revisionDate;
    @Column(name = "dealName", length = 125)
    @Size(max = 125, message = "Only 125 characters allowed")
    private String dealName;
    @Column(name = "dealType", length = 125)
    @Size(max = 125, message = "Only 125 characters allowed")
    private String dealType;
    @Column(name = "sourceListId", length = 125)
    @Size(max = 125, message = "Only 125 characters allowed")
    private String sourceListId;
    @Column(name = "side", length = 125)
    @Size(max = 125, message = "Only 125 characters allowed")
    private String side;

//    public BidList(BidListParameter bidListParameter) {
//        this.account = bidListParameter.getAccount();
//        this.type = bidListParameter.getType();
//        this.bidQuantity = bidListParameter.getBidQuantity();
//    }
}
