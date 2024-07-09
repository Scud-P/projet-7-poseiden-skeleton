package com.nnk.springboot;

import com.nnk.springboot.domain.DTO.TradeDTO;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.domain.parameter.TradeParameter;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.services.TradeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TradeServiceTest {

    @Autowired
    private TradeService tradeService;

    @MockBean
    private TradeRepository tradeRepository;

    private static Trade trade;
    private static Trade trade2;

    private static List<Trade> tradeList;
    private static TradeParameter tradeParameter;
    private static Trade simplifiedTrade;

    @BeforeEach
    public void setUp() {

        int id = 1;
        String account = "account";
        String type = "type";
        double buyQuantity = 1.0;
        double sellQuantity = 1.0;
        double buyPrice = 1.0;
        double sellPrice = 1.0;
        Timestamp tradeDate = new Timestamp(System.currentTimeMillis());
        String security = "security";
        String status = "status";
        String trader = "trader";
        String benchmark = "benchmark";
        String book = "book";
        String creationName = "creationName";
        Timestamp creationDate = new Timestamp(System.currentTimeMillis());
        String revisionName = "revisionName";
        Timestamp revisionDate = new Timestamp(System.currentTimeMillis());
        String dealName = "dealName";
        String dealType = "dealType";
        String sourceListId = "sourceListId";
        String side = "side";

        trade = new Trade(id, account, type, buyQuantity, sellQuantity, buyPrice, sellPrice, tradeDate,
                security, status, trader, benchmark, book, creationName, creationDate, revisionName, revisionDate,
                dealName, dealType, sourceListId, side);

        int id2 = 2;
        String account2 = "account2";
        String type2 = "type2";
        double buyQuantity2 = 2.0;
        double sellQuantity2 = 2.0;
        double buyPrice2 = 2.0;
        double sellPrice2 = 2.0;
        Timestamp tradeDate2 = new Timestamp(System.currentTimeMillis());
        String security2 = "security2";
        String status2 = "status2";
        String trader2 = "trader2";
        String benchmark2 = "benchmark2";
        String book2 = "book2";
        String creationName2 = "creationName2";
        Timestamp creationDate2 = new Timestamp(System.currentTimeMillis());
        String revisionName2 = "revisionName2";
        Timestamp revisionDate2 = new Timestamp(System.currentTimeMillis());
        String dealName2 = "dealName2";
        String dealType2 = "dealType2";
        String sourceListId2 = "sourceListId2";
        String side2 = "side2";

        trade2 = new Trade(id2, account2, type2, buyQuantity2, sellQuantity2, buyPrice2, sellPrice2, tradeDate2,
                security2, status2, trader2, benchmark2, book2, creationName2, creationDate2, revisionName2, revisionDate2,
                dealName2, dealType2, sourceListId2, side2);

        tradeList = List.of(trade, trade2);

        simplifiedTrade = new Trade();
        simplifiedTrade.setId(1);
        simplifiedTrade.setAccount(account);
        simplifiedTrade.setType(type);
        simplifiedTrade.setBuyQuantity(buyQuantity);

        tradeParameter = new TradeParameter();
        tradeParameter.setId(1);
        tradeParameter.setAccount(account);
        tradeParameter.setType(type);
        tradeParameter.setBuyQuantity(buyQuantity);

    }

    @Test
    public void testGetTradeById() {

        tradeService.addTrade(tradeParameter);

        when(tradeRepository.findById(trade.getId())).thenReturn(Optional.ofNullable(trade));
        Trade foundTrade = tradeService.getTradeById(trade.getId());

        assertEquals(foundTrade, trade);
    }

    @Test
    public void testGetAllTrades() {
        when(tradeRepository.findAll()).thenReturn(tradeList);
        List<TradeDTO> foundList = tradeService.getAllTrades();

        assertEquals(tradeList.size(), foundList.size());
    }

    @Test
    public void testAddTrade() {
        Trade addedTrade = tradeService.addTrade(tradeParameter);
        simplifiedTrade.setCreationDate(trade.getCreationDate());

        verify(tradeRepository, times(1)).save(any(Trade.class));

        assertEquals(simplifiedTrade.getId(), addedTrade.getId());
        assertEquals(simplifiedTrade.getAccount(), addedTrade.getAccount());
        assertEquals(simplifiedTrade.getType(), addedTrade.getType());
        assertEquals(simplifiedTrade.getCreationDate().getTime(), addedTrade.getCreationDate().getTime(), 10);
    }

    @Test
    public void testUpdateTradeFoundTrade() {

        tradeService.addTrade(tradeParameter);

        when(tradeRepository.findById(simplifiedTrade.getId())).thenReturn(Optional.ofNullable(trade));

        TradeParameter updatedTradeParameter = new TradeParameter();
        updatedTradeParameter.setId(1);
        updatedTradeParameter.setAccount(trade2.getAccount());
        updatedTradeParameter.setType(trade2.getType());
        updatedTradeParameter.setBuyQuantity(trade2.getBuyQuantity());

        Trade resultingTrade = tradeService.updateTrade(1, updatedTradeParameter);

        Timestamp now = new Timestamp(System.currentTimeMillis());

        assertEquals(simplifiedTrade.getId(), resultingTrade.getId());
        assertEquals(trade2.getAccount(), resultingTrade.getAccount());
        assertEquals(trade2.getType(), resultingTrade.getType());
        assertEquals(now.getTime(), resultingTrade.getRevisionDate().getTime(), 10);
        assertEquals(trade.getCreationDate(), resultingTrade.getCreationDate());

        verify(tradeRepository, times(2)).save(any(Trade.class));
    }

    @Test
    public void testUpdateTradeNotFound() {

        TradeParameter wrongTradeParameter = new TradeParameter();
        wrongTradeParameter.setId(trade2.getId());
        wrongTradeParameter.setAccount(trade2.getAccount());
        wrongTradeParameter.setType(trade2.getType());
        wrongTradeParameter.setBuyQuantity(trade2.getBuyQuantity());

        assertThrows(IllegalArgumentException.class, () -> tradeService.updateTrade(trade.getId(), wrongTradeParameter));
    }

    @Test
    public void testDeleteFoundTrade() {

        when(tradeRepository.findById(anyInt())).thenReturn(Optional.ofNullable(trade));
        tradeService.deleteTrade(trade.getId());

        verify(tradeRepository, times(1)).delete(trade);
    }

    @Test
    public void testDeleteTradeNotFound() {
        assertThrows(IllegalArgumentException.class, () -> tradeService.deleteTrade(trade.getId()));
        verify(tradeRepository, times(0)).delete(trade);
    }

    @Test
    public void testGetTradeByIdNotFound() {
        assertThrows(IllegalArgumentException.class, () -> tradeService.getTradeById(trade.getId()));
    }
}


