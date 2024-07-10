package com.nnk.springboot.services;

import com.nnk.springboot.domain.DTO.TradeDTO;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.domain.parameter.TradeParameter;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.util.TradeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
public class TradeService {

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private TradeMapper tradeMapper;


    public List<TradeDTO> getAllTrades() {
        return tradeRepository.findAll()
                .stream()
                .map(tradeMapper::toTradeDTO)
                .toList();
    }

    @Transactional
    public Trade addTrade(TradeParameter tradeParameter) {
        Timestamp creationDate = new Timestamp(System.currentTimeMillis());
        Trade trade = tradeMapper.toTrade(tradeParameter);
        trade.setCreationDate(creationDate);
        tradeRepository.save(trade);
        return trade;
    }

    public Trade getTradeById(int id) {
        return tradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No trade found for ID " + id));
    }

    @Transactional
    public Trade updateTrade(int id, TradeParameter tradeParameter) {
        Trade tradeToUpdate = getTradeById(id);
        tradeToUpdate.setAccount(tradeParameter.getAccount());
        tradeToUpdate.setType(tradeParameter.getType());
        tradeToUpdate.setBuyQuantity(tradeParameter.getBuyQuantity());
        tradeToUpdate.setRevisionDate(new Timestamp(System.currentTimeMillis()));
        tradeRepository.save(tradeToUpdate);
        return tradeToUpdate;
    }

    @Transactional
    public void deleteTrade(int id) {
        Trade tradeToDelete = getTradeById(id);
        tradeRepository.delete(tradeToDelete);
    }

    public TradeDTO getTradeDTOById(int id) {
        Trade trade = tradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Trade found for Id " + id));
        return tradeMapper.toTradeDTO(trade);
    }

    public TradeParameter getTradeParameterById(int id) {
        Trade trade = tradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Trade found for Id " + id));
        return tradeMapper.toTradeParameter(trade);
    }
}
