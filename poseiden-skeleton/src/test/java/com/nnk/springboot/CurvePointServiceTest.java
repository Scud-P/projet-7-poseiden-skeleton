package com.nnk.springboot;

import com.nnk.springboot.domain.CurvePoint;
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
    }


    @Test
    public void testGetAllCurvePoints() {

        List<CurvePoint> curvePoints = List.of(curvePoint, secondPoint);

        when(curvePointRepository.findAll()).thenReturn(curvePoints);

        List<CurvePoint> foundCurvePoints = curvePointService.getAllCurvePoints();

        assertEquals(curvePoints.size(), foundCurvePoints.size());
        verify(curvePointRepository, times(1)).findAll();

    }

    @Test
    public void testGetCurvePointById() {

        when(curvePointRepository.save(curvePoint)).thenReturn(curvePoint);
        CurvePoint existingCurvePoint = curvePointService.addCurvePoint(curvePoint);

        when(curvePointRepository.findById(curvePoint.getId())).thenReturn(Optional.ofNullable(curvePoint));
        CurvePoint foundCurvePoint = curvePointService.getCurvePointById(curvePoint.getId());

        assertEquals(existingCurvePoint.getId(), foundCurvePoint.getId());
        assertEquals(existingCurvePoint.getCurveId(), foundCurvePoint.getCurveId());
        assertEquals(existingCurvePoint.getTerm(), foundCurvePoint.getTerm());
        assertEquals(existingCurvePoint.getValue(), foundCurvePoint.getValue());
        assertEquals(existingCurvePoint.getAsOfDate(), foundCurvePoint.getAsOfDate());
        assertEquals(existingCurvePoint.getCreationDate(), foundCurvePoint.getCreationDate());
    }

    @Test
    public void testAddCurvePoint() {

        when(curvePointRepository.save(curvePoint)).thenReturn(curvePoint);

        CurvePoint addedCurvePoint = curvePointService.addCurvePoint(curvePoint);

        verify(curvePointRepository, times(1)).save(curvePoint);
        assertEquals(curvePoint, addedCurvePoint);
    }

    @Test
    public void testDeleteCurvePointExistingCurvePoint() {

        when(curvePointRepository.save(curvePoint)).thenReturn(curvePoint);
        curvePointService.addCurvePoint(curvePoint);
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

        testAddCurvePoint();

        int id = curvePoint.getId();

        when(curvePointRepository.findById(id)).thenReturn(Optional.ofNullable(curvePoint));

        CurvePoint updatedCurvePoint = new CurvePoint();
        updatedCurvePoint.setCurveId(20);
        updatedCurvePoint.setTerm(2.0);
        updatedCurvePoint.setValue(3.0);

        CurvePoint resultingCurvePoint = curvePointService.updateCurvePoint(id, updatedCurvePoint);

        Timestamp now = new Timestamp(System.currentTimeMillis());

        assertEquals(20, resultingCurvePoint.getCurveId());
        assertEquals(2.0, resultingCurvePoint.getTerm());
        assertEquals(3.0, resultingCurvePoint.getValue());
        assertEquals(now.getTime(), resultingCurvePoint.getAsOfDate().getTime(), 10);
        assertEquals(curvePoint.getCreationDate(), resultingCurvePoint.getCreationDate());

        verify(curvePointRepository, times(2)).save(curvePoint);
    }
    @Test
    public void testUpdateCurvePointNotFound() {

        assertThrows(IllegalArgumentException.class, () -> curvePointService.updateCurvePoint(1, curvePoint));
        verify(curvePointRepository, never()).save(curvePoint);

    }

    @Test
    public void testGetCurvePointByIdNotFound() {
        assertThrows(IllegalArgumentException.class, () -> curvePointService.getCurvePointById(curvePoint.getId()));
    }

}
