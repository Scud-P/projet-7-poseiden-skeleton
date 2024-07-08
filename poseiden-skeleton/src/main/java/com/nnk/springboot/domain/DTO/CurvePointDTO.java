package com.nnk.springboot.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurvePointDTO {

    private int id;
    private int curveId;
    private double term;
    private double value;

}
