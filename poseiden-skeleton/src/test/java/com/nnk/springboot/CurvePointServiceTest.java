package com.nnk.springboot;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.domain.DTO.CurvePointDTO;
import com.nnk.springboot.domain.parameter.CurvePointParameter;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.services.CurvePointService;
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
public class CurvePointServiceTest {

    @Autowired
    private CurvePointService curvePointService;

    @MockBean
    private CurvePointRepository curvePointRepository;

    private static CurvePoint curvePoint;
    private static CurvePoint secondPoint;
    private static CurvePoint simplifiedCurvePoint;
    private static CurvePointParameter curvePointParameter;

    @BeforeEach
    public void SetUp() {

        int id = 1;
        int id2 = 2;
        Integer curveId = 10;
        Integer curveId2 = 20;
        Timestamp firstStamp = new Timestamp(1000000000000L);
        Timestamp secondStamp = new Timestamp(1111111111111L);
        Double term = 1.0;
        Double value = 1.0;

        curvePoint = new CurvePoint(id, curveId, firstStamp, term, value, secondStamp);
        secondPoint = new CurvePoint(id2, curveId2, firstStamp, term, value, secondStamp);

        simplifiedCurvePoint = new CurvePoint();
        simplifiedCurvePoint.setId(id);
        simplifiedCurvePoint.setCurveId(curveId);
        simplifiedCurvePoint.setValue(value);
        simplifiedCurvePoint.setTerm(term);

        curvePointParameter = new CurvePointParameter(id, curveId, term, value);
    }


    @Test
    public void testGetAllCurvePoints() {

        List<CurvePoint> curvePoints = List.of(curvePoint, secondPoint);

        when(curvePointRepository.findAll()).thenReturn(curvePoints);

        List<CurvePointDTO> foundCurvePoints = curvePointService.getAllCurvePoints();

        assertEquals(curvePoints.size(), foundCurvePoints.size());
        verify(curvePointRepository, times(1)).findAll();

    }

    @Test
    public void testGetCurvePointById() {

        curvePointService.addCurvePoint(curvePointParameter);

        when(curvePointRepository.findById(curvePoint.getId())).thenReturn(Optional.ofNullable(curvePoint));

        CurvePoint foundPoint = curvePointService.getCurvePointById(curvePoint.getId());
        assertEquals(curvePoint, foundPoint);
    }

    @Test
    public void testAddCurvePoint() {

        CurvePoint addedCurvePoint = curvePointService.addCurvePoint(curvePointParameter);
        simplifiedCurvePoint.setCreationDate(addedCurvePoint.getCreationDate());
        simplifiedCurvePoint.setAsOfDate(addedCurvePoint.getCreationDate());
        verify(curvePointRepository, times(1)).save(simplifiedCurvePoint);
        assertEquals(simplifiedCurvePoint, addedCurvePoint);

    }

    @Test
    public void testDeleteCurvePointExistingCurvePoint() {

        curvePointService.addCurvePoint(curvePointParameter);
        when(curvePointRepository.findById(curvePoint.getId())).thenReturn(Optional.ofNullable(curvePoint));

        curvePointService.deleteCurvePoint(curvePoint.getId());

        verify(curvePointRepository, times(1)).delete(curvePoint);
    }

    @Test
    public void testDeleteCurvePointNotFound() {

        assertThrows(IllegalArgumentException.class, () -> curvePointService.deleteCurvePoint(curvePoint.getId()));
        verify(curvePointRepository, never()).delete(any(curvePoint.getClass()));
    }

    @Test
    public void testUpdateCurvePointExistingCurvePoint() {

        curvePointService.addCurvePoint(curvePointParameter);

        when(curvePointRepository.findById(curvePoint.getId())).thenReturn(Optional.ofNullable(curvePoint));

        CurvePointParameter updatedCurvePointParameter = new CurvePointParameter();
        updatedCurvePointParameter.setId(1);
        updatedCurvePointParameter.setCurveId(20);
        updatedCurvePointParameter.setTerm(2.0);
        updatedCurvePointParameter.setValue(3.0);

        CurvePoint resultingCurvePoint = curvePointService.updateCurvePoint(curvePoint.getId(), updatedCurvePointParameter);

        when(curvePointRepository.save(any(CurvePoint.class))).thenAnswer(invocation -> invocation.getArgument(0));


        Timestamp now = new Timestamp(System.currentTimeMillis());

        assertEquals(20, resultingCurvePoint.getCurveId());
        assertEquals(2.0, resultingCurvePoint.getTerm());
        assertEquals(3.0, resultingCurvePoint.getValue());
        assertEquals(now.getTime(), resultingCurvePoint.getAsOfDate().getTime(), 10);
        assertEquals(curvePoint.getCreationDate(), resultingCurvePoint.getCreationDate());

        verify(curvePointRepository, times(2)).save(any(CurvePoint.class));
    }

    @Test
    public void testUpdateCurvePointNotFound() {

        assertThrows(IllegalArgumentException.class, () -> curvePointService.updateCurvePoint(1, curvePointParameter));
        verify(curvePointRepository, never()).save(curvePoint);

    }

    @Test
    public void testGetCurvePointByIdNotFound() {
        assertThrows(IllegalArgumentException.class, () -> curvePointService.getCurvePointById(curvePoint.getId()));
    }

    @Test
    public void testGetCurvePointDTOById() {
        CurvePointDTO curvePointDTO = new CurvePointDTO(
                curvePoint.getId(), curvePoint.getCurveId(), curvePoint.getValue(), curvePoint.getTerm());

        when(curvePointRepository.findById(anyInt())).thenReturn(Optional.ofNullable(curvePoint));

        CurvePointDTO result = curvePointService.getCurvePointDTOById(curvePoint.getId());

        assertEquals(curvePointDTO, result);
        verify(curvePointRepository, times(1)).findById(curvePoint.getId());
    }

    @Test
    public void testGetCurvePointDTOByIdNotFound() {
        assertThrows(IllegalArgumentException.class, () -> curvePointService.getCurvePointDTOById(5));
        verify(curvePointRepository, times(1)).findById(5);

    }

    @Test
    public void testGetCurvePointParamById() {
        CurvePointParameter curvePointParam = new CurvePointParameter(
                curvePoint.getId(), curvePoint.getCurveId(), curvePoint.getValue(), curvePoint.getTerm());

        when(curvePointRepository.findById(anyInt())).thenReturn(Optional.ofNullable(curvePoint));

        CurvePointParameter result = curvePointService.getCurvePointParameterById(curvePoint.getId());

        assertEquals(curvePointParam, result);
        verify(curvePointRepository, times(1)).findById(curvePoint.getId());
    }

    @Test
    public void testGetCurvePointParamByIdNotFound() {
        assertThrows(IllegalArgumentException.class, () -> curvePointService.getCurvePointParameterById(5));
        verify(curvePointRepository, times(1)).findById(5);
    }
}
