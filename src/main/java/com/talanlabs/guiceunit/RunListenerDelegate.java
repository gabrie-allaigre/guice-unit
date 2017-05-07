package com.talanlabs.guiceunit;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

import java.util.List;

public class RunListenerDelegate extends RunListener {

    private final List<TestListener> testListeners;
    private final TestContext testContext;

    public RunListenerDelegate(List<TestListener> testListeners, TestContext testContext) {
        super();

        this.testListeners = testListeners;
        this.testContext = testContext;
    }

    @Override
    public void testStarted(Description description) throws Exception {
        testContext.setTestMethod(description.getMethodName());
        testContext.setTestSuccessful(null);

        for (TestListener testListener : testListeners) {
            testListener.beforeTestMethod(testContext);
        }
    }

    @Override
    public void testFinished(Description description) throws Exception {
        for (int i = testListeners.size() - 1; i >= 0; i--) {
            testListeners.get(i).afterTestMethod(testContext);
        }
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        testContext.setTestMethod(null);
        for (int i = testListeners.size() - 1; i >= 0; i--) {
            testListeners.get(i).afterTestClass(testContext);
        }
    }
}
