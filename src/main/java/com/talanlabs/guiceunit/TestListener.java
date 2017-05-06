package com.talanlabs.guiceunit;

public interface TestListener {

    void beforeTestClass(TestContext testContext) throws Exception;

    void prepareTestInstance(TestContext testContext) throws Exception;

    void beforeTestMethod(TestContext testContext) throws Exception;

    void afterTestMethod(TestContext testContext) throws Exception;

    void afterTestClass(TestContext testContext) throws Exception;

}
