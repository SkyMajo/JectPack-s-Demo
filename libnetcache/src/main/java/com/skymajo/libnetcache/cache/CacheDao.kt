package com.skymajo.libnetcache.cache

import androidx.room.*

//为什么CacheDao一定要写成抽象类或者接口？
//因为在编译时，Room会自动帮我们生成代码，
// 所以Room需要一个可以继承实现并复写的类，
// 方便room复写具体实现方法；

@Dao
abstract class BaseDao<T>{

    //Insert注解中onConflict作用是如果插入发生了冲突(冲突只存在于主键中)的解决方式
    //REPLACE 替换老的，使用新的
    //ROLLBACK回滚，使用老的
    //ABORT 终止操作，使用老的
    //FAIL 提交失败
    //IGNORE 忽略冲突


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    open abstract fun sava(entry: T)

    //"select * from cache where `key`=:key"
    //从cache表中查询，``中的key是表中列的名称，第二个key是传入数据
    //爆红的原因是此时表还没有映射过去，也就是数据库数据表还没有创建
//    @Query("select *from cache where `key`=:key")
//    open abstract fun getCache(key:String):Cache

    @Delete
    open abstract fun delete(cache:Cache)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    open abstract fun update(cache: Cache)
}



@Dao
abstract class CacheDao : BaseDao<Cache>(){

    @Query("select *from cache where `key`=:key")
    abstract  fun getCache(key: String):Cache



}