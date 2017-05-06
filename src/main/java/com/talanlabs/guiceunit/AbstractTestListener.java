package com.talanlabs.guiceunit;

public abstract class AbstractTestListener implements TestListener {

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
    }

    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
    }

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
    }
}
