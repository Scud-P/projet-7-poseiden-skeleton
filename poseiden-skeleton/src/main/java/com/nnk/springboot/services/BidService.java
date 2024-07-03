package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
public class BidService {

    @Autowired
    private BidListRepository bidListRepository;

    public List<BidList> getAllBids() {
        return bidListRepository.findAll();
    }

    @Transactional
    public BidList addBid(BidList bid) {
        Timestamp date = new Timestamp(System.currentTimeMillis());
        bid.setBidListDate(date);
        bidListRepository.save(bid);
        return bid;
    }


    public BidList getBidById(int id) {
        return bidListRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("No Bid found for Id " + id));
    }

    @Transactional
    public BidList updateBidList(int id, BidList bidList) {
        BidList existingBid = getBidById(id);
        existingBid.setAccount(bidList.getAccount());
        existingBid.setType(bidList.getType());
        existingBid.setBidQuantity(bidList.getBidQuantity());
        return existingBid;
    }

    @Transactional
    public void deleteBid(int id) {
        BidList existingBid = getBidById(id);
        bidListRepository.delete(existingBid);
    }
}
