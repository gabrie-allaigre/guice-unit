package com.talanlabs.guiceunit;

public class TestContext {

    private final Class<?> testClass;
    private Object testInstance;
    private String testMethod;
    private Boolean testSuccessful;

    public TestContext(Class<?> testClass) {
        super();

        this.testClass = testClass;
    }

    public Class<?> getTestClass() {
        return testClass;
    }

    public Object getTestInstance() {
        return testInstance;
    }

    void setTestInstance(Object testInstance) {
        this.testInstance = testInstance;
    }

    public String getTestMethod() {
        return testMethod;
    }

    void setTestMethod(String testMethod) {
        this.testMethod = testMethod;
    }

    public Boolean getTestSuccessful() {
        return testSuccessful;
    }

    void setTestSuccessful(Boolean testSuccessful) {
        this.testSuccessful = testSuccessful;
    }
}
