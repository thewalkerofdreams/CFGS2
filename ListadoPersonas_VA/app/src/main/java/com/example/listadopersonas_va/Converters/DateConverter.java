package com.example.listadopersonas_va.Converters;

import androidx.room.TypeConverter;

import java.util.GregorianCalendar;

public class DateConverter {
    @TypeConverter
    public static GregorianCalendar fromLong(Long value){
        if(value == null){
            throw new NullPointerException("must not be null 1");
        }
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(value);
        return cal;
    }

    @TypeConverter
    public static Long toLong(GregorianCalendar calendar){
        if(calendar == null){
            throw new NullPointerException("must not be null 2");
        }

        return calendar.getTimeInMillis();
    }
}