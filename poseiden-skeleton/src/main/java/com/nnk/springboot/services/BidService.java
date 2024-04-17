package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
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
        Date date = new java.sql.Date(System.currentTimeMillis());
        bid.setBidListDate(date);
        bidListRepository.save(bid);
        return bid;
    }


    public BidList getBidById(Integer id) {
        BidList bid = bidListRepository.findById(id).
                orElseThrow( () -> new IllegalArgumentException("No Bid found for Id " + id));
        return  bid;
    }

    @Transactional
    public BidList updateBidList(Integer id, BidList bidList) {
        BidList existingBid = bidListRepository.findById(id)
                .orElseThrow( () -> new IllegalArgumentException("No Bid found for Id " + id));

        existingBid.setAccount(bidList.getAccount());
        existingBid.setType(bidList.getType());
        existingBid.setBidQuantity(bidList.getBidQuantity());

        return existingBid;
    }

    @Transactional
    public void deleteBid(BidList bidToDelete) {
        BidList existingBid = bidListRepository.findById(bidToDelete.getId()).
                orElseThrow( () -> new IllegalArgumentException("No Bid found for Id"));
        bidListRepository.delete(existingBid);
    }
}
