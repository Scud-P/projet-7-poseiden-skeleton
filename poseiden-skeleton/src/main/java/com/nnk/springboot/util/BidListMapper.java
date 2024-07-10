package com.nnk.springboot.util;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.DTO.BidListDTO;
import com.nnk.springboot.domain.parameter.BidListParameter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BidListMapper {
    BidList toBidList(BidListParameter bidListParameter);
    BidListDTO toBidListDTO(BidList bidList);
    BidListParameter toBidListParameter(BidList bidList);
}
