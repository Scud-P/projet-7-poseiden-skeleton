package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.domain.DTO.CurvePointDTO;
import com.nnk.springboot.domain.parameter.CurvePointParameter;
import com.nnk.springboot.util.CurvePointMapper;
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

    @Autowired
    private CurvePointMapper curvePointMapper;


    @Transactional
    public CurvePoint addCurvePoint(CurvePointParameter curvePointParameter) {
        Timestamp creationStamp = new Timestamp(System.currentTimeMillis());
        CurvePoint curvePoint = curvePointMapper.toCurvePoint(curvePointParameter);
        curvePoint.setCreationDate(creationStamp);
        curvePoint.setAsOfDate(creationStamp);
        curvePointRepository.save(curvePoint);
        return curvePoint;
    }

    @Transactional
    public CurvePoint updateCurvePoint(int id, CurvePointParameter curvePointParameter) {

        CurvePoint existingCurvePoint = getCurvePointById(id);
        existingCurvePoint.setCurveId(curvePointParameter.getCurveId());
        existingCurvePoint.setTerm(curvePointParameter.getTerm());
        existingCurvePoint.setValue(curvePointParameter.getValue());
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

    public List<CurvePointDTO> getAllCurvePoints() {
        return curvePointRepository.findAll()
                .stream()
                .map(curvePointMapper::toCurvePointDTO)
                .toList();
    }

    public CurvePointDTO getCurvePointDTOById(int id) {
        CurvePoint curvePoint = curvePointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No CurvePoint found for id " + id));
        return curvePointMapper.toCurvePointDTO(curvePoint);
    }

    public CurvePointParameter getCurvePointParameterById(int id) {
        CurvePoint curvePoint = curvePointRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No CurvePoint found for id " + id));
        return curvePointMapper.toCurvePointParameter(curvePoint);
    }
}
