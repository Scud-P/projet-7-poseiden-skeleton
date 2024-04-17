package com.nnk.springboot;

import com.nnk.springboot.domain.BidList;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BidListTest {

    @Test
    public void testGettersAndSetters() {

        int id = 1;
        String account = "account";
        String type = "type";
        double bidQty = 1.0;
        double askQty = 1.0;
        double bid = 1.0;
        double ask = 1.0;
        String benchmark = "benchmark";
        Date bidListDate = new Date(1000L);
        String commentary = "commentary";

        BidList bidList = new BidList();
        bidList.setId(id);
        bidList.setAccount(account);
        bidList.setType(type);
        bidList.setBidQuantity(bidQty);
        bidList.setAskQuantity(askQty);
        bidList.setBid(bid);
        bidList.setAsk(ask);
        bidList.setBenchmark(benchmark);
        bidList.setBidListDate(bidListDate);
        bidList.setCommentary(commentary);

        assertEquals(id, bidList.getId());
        assertEquals(account, bidList.getAccount());
        assertEquals(type, bidList.getType());
        assertEquals(bidQty, bidList.getBidQuantity());
        assertEquals(askQty, bidList.getAskQuantity());
        assertEquals(bid, bidList.getBid());
        assertEquals(ask, bidList.getAsk());
        assertEquals(benchmark, bidList.getBenchmark());
        assertEquals(bidListDate, bidList.getBidListDate());
        assertEquals(commentary, bidList.getCommentary());
    }

    @Test
    public void testEquals() {

        int id = 1;
        String account = "account";
        String type = "type";
        double bidQty = 1.0;
        double askQty = 1.0;
        double bid = 1.0;
        double ask = 1.0;
        String benchmark = "benchmark";
        Date bidListDate = new Date(1000L);
        String commentary = "commentary";

        BidList bidList1 = new BidList(id, account, type, bidQty, askQty, bid, ask, benchmark, bidListDate, commentary);
        BidList bidList2 = new BidList(id, account, type, bidQty, askQty, bid, ask, benchmark, bidListDate, commentary);

        assertEquals(bidList1, bidList2);
        assertEquals(bidList1.hashCode(), bidList2.hashCode());
        assertEquals(bidList1.toString(), bidList2.toString());
    }

}
