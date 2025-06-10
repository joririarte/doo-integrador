package com.ventas.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtils {
    // Formato solo fecha
    private static final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
    // Formato fecha y hora
    private static final SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // convierte "2025-06-09" en Date (sin hora específica, es 00:00:00)
    public static Date stringToDate(String strDate) {
        if (strDate == null) return null;
        try {
            return sdfDate.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    // convierte "2025-06-09 14:30:00" en Date
    public static Date stringToDateTime(String strDateTime) {
        if (strDateTime == null) return null;
        try {
            return sdfDateTime.parse(strDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    // convierte Date a "yyyy-MM-dd"
    public static String dateToString(Date date) {
        if (date == null) return null;
        return sdfDate.format(date);
    }

    // convierte Date a "yyyy-MM-dd HH:mm:ss"
    public static String dateTimeToString(Date date) {
        if (date == null) return null;
        return sdfDateTime.format(date);
    }

    public static String setWildcard(String param){
        return "%" + param + "%";
    }
}
