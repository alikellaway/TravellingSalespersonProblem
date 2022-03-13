package com.alike.solvertestsuite;

import com.alike.staticgraphsystem.Graph;

public interface TestOutput {
    int getTestNumber();
    void setTestNumber(int testNumber);
    boolean isFail();
}
