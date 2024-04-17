package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class CurvePointService {

    @Autowired
    private CurvePointRepository curvePointRepository;


    @Transactional
    public CurvePoint addCurvePoint(CurvePoint curvePoint) {
        Timestamp creationStamp = new Timestamp(System.currentTimeMillis());
        curvePoint.setCreationDate(creationStamp);
        curvePoint.setAsOfDate(creationStamp);
        curvePointRepository.save(curvePoint);
        return curvePoint;
    }

    @Transactional
    public CurvePoint updateCurvePoint(Integer id, CurvePoint updatedCurvePoint) {
        CurvePoint existingCurvePoint = curvePointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid CurvePoint ID: " + id));

        existingCurvePoint.setCurveId(updatedCurvePoint.getCurveId());
        existingCurvePoint.setTerm(updatedCurvePoint.getTerm());
        existingCurvePoint.setValue(updatedCurvePoint.getValue());
        existingCurvePoint.setAsOfDate(new Timestamp(System.currentTimeMillis()));

        curvePointRepository.save(existingCurvePoint);

        return existingCurvePoint;
    }

    public CurvePoint getCurvePointById(int id) {
        Optional<CurvePoint> optionalCurvePoint = curvePointRepository.findById(id);
        return  optionalCurvePoint.orElseThrow(() -> new IllegalArgumentException("Invalid Id"));
    }

    public void deleteCurvePoint(CurvePoint curvePointToDelete) {
        CurvePoint existingCurvePoint = curvePointRepository.findById(curvePointToDelete.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Id"));
        curvePointRepository.delete(existingCurvePoint);
    }

    public List<CurvePoint> getAllCurvePoints() {
        return curvePointRepository.findAll();
    }
}
