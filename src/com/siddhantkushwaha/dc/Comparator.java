package com.siddhantkushwaha.dc;

/*
    Since all the code is built for generic data-type,
    a comparator function is required to compare the two data values
    required in distributed sorting
*/

import com.sun.istack.internal.NotNull;

public interface Comparator<T> {
    boolean compare(@NotNull T data1, @NotNull T data2);
}