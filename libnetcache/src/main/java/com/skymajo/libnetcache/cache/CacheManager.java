package com.skymajo.libnetcache.cache;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CacheManager {
    public static <T> void save(String key, T body) {
        Cache cache = new Cache();
        cache.key = key;
        cache.data = toByteArray(body);
        CacheDataBase.get().getCache().sava(cache);

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
            Object o = ois.readObject();
            return o;
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
        try{
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(body);
            oos.flush();
        }catch (Exception e){
            e.printStackTrace();
            try {
                if (baos != null) {
                    baos.close();
                }
                if (oos != null){
                    oos.close();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return new byte[0];
    }
}
