package com.nnk.springboot.util;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.DTO.BidListDTO;
import com.nnk.springboot.domain.parameter.BidListParameter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BidListMapper {

    //TODO Check if I can remove that

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "bidListDate", ignore = true)
    @Mapping(target = "askQuantity", ignore = true)
    @Mapping(target = "bid", ignore = true)
    @Mapping(target = "ask", ignore = true)
    @Mapping(target = "benchmark", ignore = true)
    @Mapping(target = "commentary", ignore = true)
    @Mapping(target = "security", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "trader", ignore = true)
    @Mapping(target = "book", ignore = true)
    @Mapping(target = "creationName", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "revisionName", ignore = true)
    @Mapping(target = "revisionDate", ignore = true)
    @Mapping(target = "dealName", ignore = true)
    @Mapping(target = "dealType", ignore = true)
    @Mapping(target = "sourceListId", ignore = true)
    @Mapping(target = "side", ignore = true)
    BidList toBidList(BidListParameter bidListParameter);

    BidListDTO toBidListDTO(BidList bidList);

    BidListParameter toBidListParameter(BidList bidList);

    BidList toBidList(BidListDTO bidListDTO);

    BidListParameter toBidListParameter(BidListDTO bidListDTO);

}
