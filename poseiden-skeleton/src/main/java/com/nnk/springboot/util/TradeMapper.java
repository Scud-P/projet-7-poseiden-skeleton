package com.nnk.springboot.util;

import com.nnk.springboot.domain.DTO.TradeDTO;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.domain.parameter.TradeParameter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TradeMapper {

    Trade toTrade(TradeParameter tradeParameter);
    TradeDTO toTradeDTO(Trade trade);
    TradeParameter toTradeParameter(Trade trade);
}
