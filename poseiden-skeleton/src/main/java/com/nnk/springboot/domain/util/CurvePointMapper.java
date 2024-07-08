package com.nnk.springboot.domain.util;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.domain.DTO.CurvePointDTO;
import com.nnk.springboot.domain.parameter.CurvePointParameter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CurvePointMapper {

    CurvePoint toCurvePoint(CurvePointParameter curvePointParameter);
    CurvePointDTO toCurvePointDTO(CurvePoint curvePoint);
    CurvePointParameter toCurvePointParameter(CurvePointDTO curvePointDTO);

}
