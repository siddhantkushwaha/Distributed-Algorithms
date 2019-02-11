package com.siddhantkushwaha.dc;

public interface ProcessOutput<T> {

    void onRoundComplete(int processNumber, T leftData, T rightData, int area, int roundNumber);

    void onFinish(int processNumber, T leftData, T rightData, int area, int roundNumber);
}
