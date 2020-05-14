package com.skymajo.libnetcache.cache;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.Relation;
import androidx.room.TypeConverters;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Date;

//        被Entity修饰的类，代表着数据表，可以传入参数tableName修改名称
@Entity(tableName = "cache"
//        foreignKeys(外键)
//        将User表中的id和key相关联
//        onDelete(NO_ACTION, RESTRICT, SET_NULL, SET_DEFAULT, CASCADE) (
//        NO_ACTION  -》 相关联数据什么都不变,
//        RESTRICT  -》 相关联数据立即发生删除or 更新
//        SET_NULL  -》 相关联数据设置为空
//        SET_DEFAULT  -> 相关数据设置为默认值
//        CASCADE  -》 相关联数据发生删除or更新
//        RESTRICT与CASCADE区别，前者是在SQL执行时立刻执行，同步一样，而后者是在sql执行完再去执行
//        )
//        foreignKeys = {@ForeignKey(entity = User.class,parentColumns = "id",childColumns = "key",onDelete = ForeignKey.RESTRICT,onUpdate = ForeignKey.RESTRICT)},
//        indices = {@Index(value = {"key","id"})}//Index 复合组键，查询更快，增删变慢，视情况而用
)
public class Cache2 implements Serializable {

    //@Ignore 忽略，如果被Ignor标记了，就不会生成对应的字段在数据表里
    @PrimaryKey //主键
    @NonNull  String key;

    //@ColumnInfo(name = "_data") //映射成数据库中的字段名
    @NonNull
    byte[] data;
    //关联查询(子查询)
    //entity -> 关联的类
    //parentColumn(父类/父表) 和 entityCcolumn(子类/子表)值相等的时候，就会进行查询将projection的字段赋值给user对应的字段中
//@Relation(entity = User.class,parentColumn = "id",entityColumn = "id",projection = {"查询的字段名"})
//public User user;


//类型转换 会自动判断DateConverter中类型进行转换
// If you put it on a {@link Database}, all Daos and Entities in that database will be able to* use it.
// If you put it on a {@link Dao}, all methods in the Dao will be able to use it.
// If you put it on an {@link Entity}, all fields of the Entity will be able to use it.
// If you put it on an {@link Entity} field, only that field will be able to use it.
// If you put it on a {@link Dao} method, all parameters of the method will be able to use it.
// If you put it on a {@link Dao} method parameter, just that field will be able to use it.
//    @TypeConverters({DateConverter.class})
//    public Date date;

//    @PrimaryKey(autoGenerate = true) //自增长主键
//    int _id;

//    @Embedded //被Embedded注释标记的字段中的对象，该对象的内的所有字段也都会映射到他父类里面
    //比如User中的所有字段都会映射在Cache表中
//    User user;

}
