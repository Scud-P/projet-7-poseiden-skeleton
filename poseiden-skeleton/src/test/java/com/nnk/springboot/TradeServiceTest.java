package com.nnk.springboot;

import com.nnk.springboot.domain.Trade;
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

    @BeforeEach
    public void setUp() {

        int tradeId = 1;
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

        trade = new Trade(tradeId, account, type, buyQuantity, sellQuantity, buyPrice, sellPrice, tradeDate,
                security, status, trader, benchmark, book, creationName, creationDate, revisionName, revisionDate,
                dealName, dealType, sourceListId, side);

        int tradeId2 = 2;
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

        trade2 = new Trade(tradeId2, account2, type2, buyQuantity2, sellQuantity2, buyPrice2, sellPrice2, tradeDate2,
                security2, status2, trader2, benchmark2, book2, creationName2, creationDate2, revisionName2, revisionDate2,
                dealName2, dealType2, sourceListId2, side2);

        tradeList = List.of(trade, trade2);
    }

    @Test
    public void testGetTradeById() {

        when(tradeRepository.findById(trade.getTradeId())).thenReturn(Optional.ofNullable(trade));
        Trade foundTrade = tradeService.getTradeById(trade.getTradeId());

        assertEquals(foundTrade, trade);
    }

    @Test
    public void testGetAllTrades() {

        when(tradeRepository.findAll()).thenReturn(tradeList);
        List<Trade> foundList = tradeService.getAllTrades();

        assertEquals(foundList, tradeList);
    }

    @Test
    public void testAddTrade() {

        when(tradeRepository.save(trade)).thenReturn(trade);
        Trade addedTrade = tradeService.addTrade(trade);
        assertEquals(addedTrade, trade);
        verify(tradeRepository, times(1)).save(any(Trade.class));

    }

    @Test
    public void testUpdateTradeFoundTrade() {

        when(tradeRepository.save(trade)).thenReturn(trade);
        Trade addedTrade = tradeService.addTrade(trade);

        when(tradeRepository.findById(trade.getTradeId())).thenReturn(Optional.ofNullable(addedTrade));

        Trade updatedTrade = new Trade();
        updatedTrade.setAccount(trade2.getAccount());
        updatedTrade.setType(trade2.getType());
        updatedTrade.setBuyQuantity(trade2.getBuyQuantity());

        Trade resultingTrade = tradeService.updateTrade(trade.getTradeId(), updatedTrade);

        Timestamp now = new Timestamp(System.currentTimeMillis());

        assertEquals(trade.getTradeId(), resultingTrade.getTradeId());
        assertEquals(trade2.getAccount(), resultingTrade.getAccount());
        assertEquals(trade2.getType(), resultingTrade.getType());
        assertEquals(now.getTime(), resultingTrade.getRevisionDate().getTime(), 10);
        assertEquals(trade.getCreationDate(), resultingTrade.getCreationDate());

        verify(tradeRepository, times(2)).save(any(Trade.class));

    }

    @Test
    public void testUpdateTradeNotFound() {
        assertThrows(IllegalArgumentException.class, () -> tradeService.updateTrade(trade.getTradeId(), trade2));
    }

    @Test
    public void testDeleteFoundTrade() {

        when(tradeRepository.findById(anyInt())).thenReturn(Optional.ofNullable(trade));
        tradeService.deleteTrade(trade.getTradeId());

        verify(tradeRepository, times(1)).delete(trade);
    }

    @Test
    public void testDeleteTradeNotFound() {
        assertThrows(IllegalArgumentException.class, () -> tradeService.deleteTrade(trade.getTradeId()));
        verify(tradeRepository, times(0)).delete(trade);
    }

    @Test
    public void testGetTradeByIdNotFound() {
        assertThrows(IllegalArgumentException.class, () -> tradeService.getTradeById(trade.getTradeId()));
    }
}


