package com.nnk.springboot;

import com.nnk.springboot.domain.Trade;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class TradeTest {

    @Test
    public void testGettersAndSettersAndEqualsAndHashCodeForSameEntities() {

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

        Trade firstTrade = new Trade();
        firstTrade.setId(id);
        firstTrade.setAccount(account);
        firstTrade.setType(type);
        firstTrade.setBuyQuantity(buyQuantity);
        firstTrade.setSellQuantity(sellQuantity);
        firstTrade.setBuyPrice(buyPrice);
        firstTrade.setSellPrice(sellPrice);
        firstTrade.setTradeDate(tradeDate);
        firstTrade.setSecurity(security);
        firstTrade.setStatus(status);
        firstTrade.setTrader(trader);
        firstTrade.setBenchmark(benchmark);
        firstTrade.setBook(book);
        firstTrade.setCreationName(creationName);
        firstTrade.setCreationDate(creationDate);
        firstTrade.setRevisionName(revisionName);
        firstTrade.setRevisionDate(revisionDate);
        firstTrade.setDealName(dealName);
        firstTrade.setDealType(dealType);
        firstTrade.setSourceListId(sourceListId);
        firstTrade.setSide(side);

        Trade secondTrade = new Trade();
        secondTrade.setId(id);
        secondTrade.setAccount(account);
        secondTrade.setType(type);
        secondTrade.setBuyQuantity(buyQuantity);
        secondTrade.setSellQuantity(sellQuantity);
        secondTrade.setBuyPrice(buyPrice);
        secondTrade.setSellPrice(sellPrice);
        secondTrade.setTradeDate(tradeDate);
        secondTrade.setSecurity(security);
        secondTrade.setStatus(status);
        secondTrade.setTrader(trader);
        secondTrade.setBenchmark(benchmark);
        secondTrade.setBook(book);
        secondTrade.setCreationName(creationName);
        secondTrade.setCreationDate(creationDate);
        secondTrade.setRevisionName(revisionName);
        secondTrade.setRevisionDate(revisionDate);
        secondTrade.setDealName(dealName);
        secondTrade.setDealType(dealType);
        secondTrade.setSourceListId(sourceListId);
        secondTrade.setSide(side);

        assertEquals(firstTrade, secondTrade);
        assertEquals(firstTrade.hashCode(), secondTrade.hashCode());
        assertEquals(firstTrade.toString(), secondTrade.toString());

    }

    @Test
    public void testGettersAndSettersAndEqualsAndHashCodeForDifferentEntities() {

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

        Trade firstTrade = new Trade();
        firstTrade.setId(id2);
        firstTrade.setAccount(account2);
        firstTrade.setType(type2);
        firstTrade.setBuyQuantity(buyQuantity2);
        firstTrade.setSellQuantity(sellQuantity2);
        firstTrade.setBuyPrice(buyPrice2);
        firstTrade.setSellPrice(sellPrice2);
        firstTrade.setTradeDate(tradeDate2);
        firstTrade.setSecurity(security2);
        firstTrade.setStatus(status2);
        firstTrade.setTrader(trader2);
        firstTrade.setBenchmark(benchmark2);
        firstTrade.setBook(book2);
        firstTrade.setCreationName(creationName2);
        firstTrade.setCreationDate(creationDate2);
        firstTrade.setRevisionName(revisionName2);
        firstTrade.setRevisionDate(revisionDate2);
        firstTrade.setDealName(dealName2);
        firstTrade.setDealType(dealType2);
        firstTrade.setSourceListId(sourceListId2);
        firstTrade.setSide(side2);

        Trade secondTrade = new Trade();
        secondTrade.setId(id);
        secondTrade.setAccount(account);
        secondTrade.setType(type);
        secondTrade.setBuyQuantity(buyQuantity);
        secondTrade.setSellQuantity(sellQuantity);
        secondTrade.setBuyPrice(buyPrice);
        secondTrade.setSellPrice(sellPrice);
        secondTrade.setTradeDate(tradeDate);
        secondTrade.setSecurity(security);
        secondTrade.setStatus(status);
        secondTrade.setTrader(trader);
        secondTrade.setBenchmark(benchmark);
        secondTrade.setBook(book);
        secondTrade.setCreationName(creationName);
        secondTrade.setCreationDate(creationDate);
        secondTrade.setRevisionName(revisionName);
        secondTrade.setRevisionDate(revisionDate);
        secondTrade.setDealName(dealName);
        secondTrade.setDealType(dealType);
        secondTrade.setSourceListId(sourceListId);
        secondTrade.setSide(side);

        assertNotEquals(firstTrade, secondTrade);
        assertNotEquals(firstTrade.hashCode(), secondTrade.hashCode());
        assertNotEquals(firstTrade.toString(), secondTrade.toString());
    }

}
