package com.nnk.springboot.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeDTO {
    private int id;
    private String account;
    private String type;
    private double buyQuantity;
}
