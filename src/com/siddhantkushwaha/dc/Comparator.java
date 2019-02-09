package com.siddhantkushwaha.dc;


import com.sun.istack.internal.NotNull;

public interface Comparator<T> {
    boolean compare(@NotNull T data1, @NotNull T data2);
}