package com.skymajo.libnetcache.cache;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {

    @TypeConverter
    //类型转换注解，要求必须要有一个参数和一个返回值
    public static Long date2Long(Date date){
        return date.getTime();
    }

    @TypeConverter
    public static Date long2Date (Long time){
        return new Date(time);
    }
}
