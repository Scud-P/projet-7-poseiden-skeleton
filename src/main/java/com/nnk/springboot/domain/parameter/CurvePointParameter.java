package com.nnk.springboot.domain.parameter;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurvePointParameter {

    private int id;
    @NotNull(message = "curveId is mandatory")
    @Min(value = 0, message = "Only positive values from 0 to 255")
    @Max(value = 255, message = "Only positive values from 0 to 255")
    private int curveId;
    private double term;
    private double value;

}
