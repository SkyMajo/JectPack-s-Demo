package com.skymajo.libnetcache.cache;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CacheManager {
    public static <T> void save(String key, T body) {
        Log.e("CacheManager","body:"+body.toString());
        Cache cache = new Cache();
        cache.key = key;
        cache.data = toByteArray(body);
        Log.e("CacheManager","CacheData:"+ cache.data.length);
        CacheDataBase.get().getCache().save(cache);

    }

    public static Object getCache(String key){
        Cache cache = CacheDataBase.get().getCache().getCache(key);
        if(cache != null && cache.data != null){
            return toObject(cache.data);
        }
        return null;
    }

    private static Object toObject(byte[] cache) {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            bais = new ByteArrayInputStream(cache);
            ois = new ObjectInputStream(bais);
            return ois.readObject();
        }catch (Exception e){
            e.printStackTrace();
            try{
                 if (bais != null) {
                    bais.close();
                }
                 if (ois != null){
                     ois.close();
                 }
            }catch (Exception e1){
                e1.printStackTrace();
            }
        }
        return null;
    }



    private static <T> byte[] toByteArray(T body) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(body);
            oos.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            Log.e("CacheManager","Obj->Byte[]'s Error"+e.fillInStackTrace());
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (oos != null) {
                    oos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new byte[0];
    }
}
