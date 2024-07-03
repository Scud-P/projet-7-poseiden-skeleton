package com.nnk.springboot;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.services.BidService;
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
public class BidListServiceTest {

    @Autowired
    private BidService bidService;

    @MockBean
    private BidListRepository bidListRepository;

    private static BidList bidList;

    @BeforeEach
    public void setUp() {

        int id = 1;
        String account = "account";
        String type = "type";
        double bidQuantity = 1.0;
        double askQuantity = 1.0;
        double bid = 1.0;
        double ask = 1.0;
        String benchmark = "benchmark";
        Timestamp bidListDate = new Timestamp(1000L);
        String commentary = "commentary";
        String security = "security";
        String status = "status";
        String trader = "trader";
        String book = "book";
        String creationName = "creationName";
        Timestamp creationDate = new Timestamp(2000L);
        String revisionName = "revisionName";
        Timestamp revisionDate = new Timestamp(3000L);
        String dealName = "dealName";
        String dealType = "dealType";
        String sourceListId = "sourceListId";
        String side = "side";


        bidList = new BidList(id, account, type, bidQuantity, askQuantity, bid, ask, benchmark, bidListDate, commentary,
                security, status, trader, book, creationName, creationDate, revisionName, revisionDate, dealName, dealType, sourceListId, side);
    }

    @Test
    public void testAddBidList() {
        BidList addedBidList = bidService.addBid(bidList);
        verify(bidListRepository, times(1)).save(bidList);
        assertEquals(bidList, addedBidList);
    }

    @Test
    public void testGetBidById() {
        bidService.addBid(bidList);

        when(bidListRepository.findById(bidList.getId())).thenReturn(Optional.ofNullable(bidList));
        BidList foundBid = bidService.getBidById(bidList.getId());
        assertEquals(bidList, foundBid);
    }

    @Test
    public void testGetBidByIdNotFound() {
        assertThrows(IllegalArgumentException.class, () -> bidService.getBidById(1));
    }

    @Test
    public void testDeleteFoundBid() {
        bidService.addBid(bidList);
        when(bidListRepository.findById(bidList.getId())).thenReturn(Optional.ofNullable(bidList));

        bidService.deleteBid(bidList.getId());

        verify(bidListRepository, times(1)).delete(bidList);
    }

    @Test
    public void testDeleteBidNotFound() {
        assertThrows(IllegalArgumentException.class, () -> bidService.deleteBid(bidList.getId()));
        verify(bidListRepository, never()).delete(any(BidList.class));
    }

    @Test
    public void testUpdateFoundBid() {

        bidService.addBid(bidList);

        when(bidListRepository.findById(bidList.getId())).thenReturn(Optional.ofNullable(bidList));

        BidList updatedBidList = new BidList();
        updatedBidList.setAccount("updatedAccount");
        updatedBidList.setType("updatedType");
        updatedBidList.setBidQuantity(2.0);

        BidList resultingBidList = bidService.updateBidList(1, updatedBidList);

        assertEquals(updatedBidList.getAccount(), resultingBidList.getAccount());
        assertEquals(updatedBidList.getType(), resultingBidList.getType());
        assertEquals(updatedBidList.getBidQuantity(), resultingBidList.getBidQuantity());

        assertEquals(bidList.getId(), resultingBidList.getId());
    }

    @Test
    public void testUpdateBidNotFound() {

        assertThrows(IllegalArgumentException.class, () -> bidService.updateBidList(1, bidList));
        verify(bidListRepository, never()).save(any(BidList.class));
    }

    @Test
    public void testGetAllBids() {

        BidList secondBidList = bidList;
        List<BidList> allBids = List.of(bidList, secondBidList);

        when(bidListRepository.findAll()).thenReturn(allBids);

        List<BidList> foundBids =  bidService.getAllBids();
        assertEquals(2, foundBids.size());
    }
}
