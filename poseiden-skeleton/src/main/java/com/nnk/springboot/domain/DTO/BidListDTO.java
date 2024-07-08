package com.nnk.springboot.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BidListDTO {

    private Integer id;
    private String account;
    private String type;
    private Double bidQuantity;

}
