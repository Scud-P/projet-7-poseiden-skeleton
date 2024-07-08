package com.nnk.springboot.domain.parameter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BidListParameter {

    private Integer id;
    @NotBlank(message = "Account is mandatory")
    @Size(max = 30, message = "Account must be at most 30 characters")
    private String account;
    @NotBlank(message = "Type is mandatory")
    @Size(max = 30, message = "Type must be at most 30 characters")
    private String type;
    private Double bidQuantity;
}
