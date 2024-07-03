package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

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

        CurvePoint existingCurvePoint = getCurvePointById(id);
        existingCurvePoint.setCurveId(updatedCurvePoint.getCurveId());
        existingCurvePoint.setTerm(updatedCurvePoint.getTerm());
        existingCurvePoint.setValue(updatedCurvePoint.getValue());
        existingCurvePoint.setAsOfDate(new Timestamp(System.currentTimeMillis()));

        curvePointRepository.save(existingCurvePoint);

        return existingCurvePoint;
    }

    public CurvePoint getCurvePointById(int id) {
        return curvePointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Id"));
    }

    @Transactional
    public void deleteCurvePoint(int id) {
        CurvePoint existingCurvePoint = getCurvePointById(id);
        curvePointRepository.delete(existingCurvePoint);
    }

    public List<CurvePoint> getAllCurvePoints() {
        return curvePointRepository.findAll();
    }
}
