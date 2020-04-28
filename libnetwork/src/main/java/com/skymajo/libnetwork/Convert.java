package com.skymajo.libnetwork;

import java.lang.reflect.Type;

public interface Convert<T> {
    T convert(String reponse , Type type);
    T convert(String reponse , Class clz);
}
