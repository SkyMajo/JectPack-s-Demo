package com.skymajo.libnetcache.cache;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.skymajo.libcommon.AppGlobals;

@Database(entities = {Cache.class},version = 1,exportSchema = true)
public abstract class CacheDataBase extends RoomDatabase {
    private static final CacheDataBase skyMajo_cacahe;
    static {
        //创建一个内存数据库
        //但是这种数据库数据只存在于内存中，也就是进程被杀后，数据随之丢失。
//        Room.inMemoryDatabaseBuilder()
        skyMajo_cacahe = Room.databaseBuilder(AppGlobals.INSTANCE.getApplication(), CacheDataBase.class, "skyMajo_cacahe")
                //是否允许在主线程运行
                .allowMainThreadQueries()
                //数据库创建和打开后的回调
//                .addCallback()
                //设置查询的线程池
//                .setQueryExecutor()
                //Room的日志模式
//                .setJournalMode()
//                数据库升级异常之后的回滚
//                    .fallbackToDestructiveMigration()
                //数据库升级异常后根据指定版本回滚
//                    .fallbackToDestructiveMigrationFrom()
                .addMigrations(CacheDataBase.migration)
                .build();


    }

    public abstract CacheDao getCache();

    public static CacheDataBase get(){
        return skyMajo_cacahe;
    }


    static Migration migration = new Migration(1,3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("alter table teacher rename to student");
            database.execSQL("alter table teacher add column teacher_age INTEGER NOT NULL default 0");
        }
    };
}
