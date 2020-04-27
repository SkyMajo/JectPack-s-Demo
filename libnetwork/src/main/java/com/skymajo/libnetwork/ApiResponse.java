package com.skymajo.libnetwork;

import javax.print.DocFlavor;

public class ApiResponse<T> {
    public boolean success;
    public int status;
    public String message;
    public T body;
}
