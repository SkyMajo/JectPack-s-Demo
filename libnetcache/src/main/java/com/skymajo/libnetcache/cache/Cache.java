package com.skymajo.libnetcache.cache;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "cache")
public class Cache implements Serializable {
    @PrimaryKey
    @NonNull
    public String key;

    //@ColumnInfo(name = "_data")
    public byte[] data;

}
