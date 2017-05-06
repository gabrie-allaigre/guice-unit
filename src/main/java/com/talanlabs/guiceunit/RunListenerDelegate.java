package com.talanlabs.guiceunit;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

public class RunListenerDelegate extends RunListener {

    private final TestListener testListener;
    private final TestContext testContext;

    public RunListenerDelegate(TestListener testListener, TestContext testContext) {
        super();

        this.testListener = testListener;
        this.testContext = testContext;
    }

    @Override
    public void testStarted(Description description) throws Exception {
        testContext.setTestMethod(description.getMethodName());
        testContext.setTestSuccessful(null);
        testListener.beforeTestMethod(testContext);
    }

    @Override
    public void testFinished(Description description) throws Exception {
        testListener.afterTestMethod(testContext);
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        testContext.setTestMethod(null);
        testListener.afterTestClass(testContext);
    }
}
