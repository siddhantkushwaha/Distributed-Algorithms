package com.siddhantkushwaha.dc;

/*
    This interface has two methods in case we want to show some output from the Process class
*/

public interface ProcessOutput<T> {

    /* This method is called by a process each time the process completes 1 round */
    void onRoundComplete(int processNumber, T leftData, T rightData, int area, int roundNumber);

    /* This method is called upon complete execution of the process */
    void onFinish(int processNumber, T leftData, T rightData, int area, int roundNumber);
}
