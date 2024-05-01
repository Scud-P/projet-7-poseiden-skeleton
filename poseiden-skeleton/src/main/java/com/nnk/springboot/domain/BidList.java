package com.nnk.springboot.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Table(name = "bidlist")
@Data
public class BidList {


    public BidList() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;


    @NotBlank(message = "Account is mandatory")
    @Column(name = "account")
    private String account;

    @NotBlank(message = "Type is mandatory")
    @Column(name = "type")
    private String type;

    @Column(name = "bidQuantity")
    private double bidQuantity;
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

    public BidList(int id, String account, String type, double bidQuantity, double askQuantity, double bid, double ask,
                   String benchmark, Timestamp bidListDate, String commentary, String security, String status, String trader,
                   String book, String creationName, Timestamp creationDate, String revisionName, Timestamp revisionDate,
                   String dealName, String dealType, String sourceListId, String side) {
        this.id = id;
        this.account = account;
        this.type = type;
        this.bidQuantity = bidQuantity;
        this.askQuantity = askQuantity;
        this.bid = bid;
        this.ask = ask;
        this.benchmark = benchmark;
        this.bidListDate = bidListDate;
        this.commentary = commentary;
        this.security = security;
        this.status = status;
        this.trader = trader;
        this.book = book;
        this.creationName = creationName;
        this.creationDate = creationDate;
        this.revisionName = revisionName;
        this.revisionDate = revisionDate;
        this.dealName = dealName;
        this.dealType = dealType;
        this.sourceListId = sourceListId;
        this.side = side;
    }

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
