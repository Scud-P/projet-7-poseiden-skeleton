package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
public class TradeService {

    @Autowired
    private TradeRepository tradeRepository;


    public List<Trade> getAllTrades() {
        return tradeRepository.findAll();
    }

    @Transactional
    public Trade addTrade(Trade tradeToAdd) {
        Timestamp creationDate = new Timestamp(System.currentTimeMillis());
        tradeToAdd.setCreationDate(creationDate);
        tradeRepository.save(tradeToAdd);
        return tradeToAdd;
    }

    public Trade getTradeById(int id) {
        return tradeRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("No trade found for ID " + id));
    }

    @Transactional
    public Trade updateTrade(int id, Trade trade) {
        Trade tradeToUpdate = getTradeById(id);
        tradeToUpdate.setAccount(trade.getAccount());
        tradeToUpdate.setType(trade.getType());
        tradeToUpdate.setBuyQuantity(trade.getBuyQuantity());
        tradeToUpdate.setRevisionDate(new Timestamp(System.currentTimeMillis()));
        tradeRepository.save(tradeToUpdate);
        return tradeToUpdate;
    }

    @Transactional
    public void deleteTrade(int id) {
        Trade tradeToDelete = getTradeById(id);
        tradeRepository.delete(tradeToDelete);
    }
}
