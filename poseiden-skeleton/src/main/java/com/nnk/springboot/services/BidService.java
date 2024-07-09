package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.DTO.BidListDTO;
import com.nnk.springboot.domain.parameter.BidListParameter;
import com.nnk.springboot.util.BidListMapper;
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

    @Autowired
    private BidListMapper bidListMapper;


    public List<BidListDTO> getAllBids() {
        return bidListRepository.findAll()
                .stream()
                .map(bidListMapper::toBidListDTO)
                .toList();
    }

    @Transactional
    public BidList addBid(BidListParameter bidListParameter) {
        Timestamp date = new Timestamp(System.currentTimeMillis());
        BidList bidList = bidListMapper.toBidList(bidListParameter);
        bidList.setBidListDate(date);
        bidListRepository.save(bidList);
        return bidList;
    }


    public BidList getBidById(int id) {
        return bidListRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Bid found for Id " + id));
    }

    public BidListDTO getBidListDTOById(int id) {
        BidList bidList = bidListRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Bid found for Id " + id));
        return bidListMapper.toBidListDTO(bidList);
    }

    public BidListParameter mapBidListDTOToParameter(BidListDTO bidListDTO) {
        return bidListMapper.toBidListParameter(bidListDTO);
    }

    @Transactional
    public BidList updateBidList(int id, BidListParameter bidListParameter) {
        BidList existingBid = getBidById(id);
        existingBid.setAccount(bidListParameter.getAccount());
        existingBid.setType(bidListParameter.getType());
        existingBid.setBidQuantity(bidListParameter.getBidQuantity());
        existingBid.setId(bidListParameter.getId());
        bidListRepository.save(existingBid);
        return existingBid;
    }

    @Transactional
    public void deleteBid(int id) {
        BidList existingBid = getBidById(id);
        bidListRepository.delete(existingBid);
    }
}
