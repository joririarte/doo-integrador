package com.ventas.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;

public class CommonUtilsTest {

    @Test
    void stringToDate_validString() {
        String dateStr = "2024-05-10";
        Date date = CommonUtils.stringToDate(dateStr);
        assertNotNull(date);
        assertEquals(dateStr, CommonUtils.dateToString(date));
    }

    @Test
    void stringToDate_invalidString() {
        assertNull(CommonUtils.stringToDate("invalid"));
    }

    @Test
    void dateToString_nullDate() {
        assertNull(CommonUtils.dateToString(null));
    }
}
