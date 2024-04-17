package com.nnk.springboot;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.domain.Rating;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class CurvePointTest {

    @Test
    public void testEqualsSameEntities() {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        CurvePoint curvePoint1 = new CurvePoint(1, 10, timestamp, 1.0, 1.0, timestamp);
        CurvePoint curvePoint2 = new CurvePoint(1, 10, timestamp, 1.0, 1.0, timestamp);

        assertEquals(curvePoint1, curvePoint2);
    }

    @Test
    public void testHashCodeSameEntities() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        CurvePoint curvePoint1 = new CurvePoint(1, 10, timestamp, 1.0, 1.0, timestamp);
        CurvePoint curvePoint2 = new CurvePoint(1, 10, timestamp, 1.0, 1.0, timestamp);

        assertEquals(curvePoint1.hashCode(), curvePoint2.hashCode());
    }

    @Test
    public void testEqualsDifferentEntities() {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        CurvePoint curvePoint1 = new CurvePoint(1, 10, timestamp, 1.0, 1.0, timestamp);
        CurvePoint curvePoint2 = new CurvePoint(2, 10, timestamp, 1.0, 1.0, timestamp);

        assertNotEquals(curvePoint1, curvePoint2);
    }

    @Test
    public void testHashCodeDifferentEntities() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        CurvePoint curvePoint1 = new CurvePoint(1, 10, timestamp, 1.0, 1.0, timestamp);
        CurvePoint curvePoint2 = new CurvePoint(2, 10, timestamp, 1.0, 1.0, timestamp);

        assertNotEquals(curvePoint1.hashCode(), curvePoint2.hashCode());

    }

}
