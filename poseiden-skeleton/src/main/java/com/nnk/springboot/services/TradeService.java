package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class TradeService {

    @Autowired
    private TradeRepository tradeRepository;


    public List<Trade> getAllTrades() {
        return tradeRepository.findAll();
    }

    public Trade addTrade(Trade tradeToAdd) {
        Timestamp creationDate = new Timestamp(System.currentTimeMillis());
        tradeToAdd.setCreationDate(creationDate);
        return tradeRepository.save(tradeToAdd);
    }

    public Trade getTradeById(Integer id) {
        return tradeRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("No trade found for ID " + id));
    }


    public Trade updateTrade(Integer id, Trade trade) {

        Trade tradeToUpdate = tradeRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("No trade found for ID " + id));
        tradeToUpdate.setAccount(trade.getAccount());
        tradeToUpdate.setType(trade.getType());
        tradeToUpdate.setBuyQuantity(trade.getBuyQuantity());
        tradeToUpdate.setRevisionDate(new Timestamp(System.currentTimeMillis()));
        tradeRepository.save(tradeToUpdate);
        return tradeToUpdate;
    }

    public void deleteTrade(Integer id) {
        Trade tradeToDelete = tradeRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("No trade found for ID " + id));
        tradeRepository.delete(tradeToDelete);
    }
}
