package com.talanlabs.guiceunit;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GuiceJunit4ClassRunner extends BlockJUnit4ClassRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuiceJunit4ClassRunner.class);

    private final transient Injector injector;
    private final transient List<TestListener> testListeners;
    private final transient TestContext testContext;

    /**
     * Creates a BlockJUnit4ClassRunner to run {@code klass}
     *
     * @param testClass Test class
     * @throws InitializationError if the test class is malformed.
     */
    public GuiceJunit4ClassRunner(Class<?> testClass) throws InitializationError {
        super(testClass);

        this.injector = createInjectorFor(getGuiceModulesFor(testClass));
        this.testListeners = findTestListener(getTestListenersFor(testClass));
        this.testContext = new TestContext(testClass);
    }

    private Injector createInjectorFor(List<Class<? extends Module>> classes) throws InitializationError {
        List<Module> modules = new ArrayList<>(classes.size());
        try {
            for (Class<? extends Module> module : classes) {
                modules.add(module.newInstance());
            }
        } catch (ReflectiveOperationException exception) {
            throw new InitializationError(exception);
        }
        return Guice.createInjector(modules);
    }

    private List<Class<? extends Module>> getGuiceModulesFor(Class<?> testClass) throws InitializationError {
        GuiceModules annotation = testClass.getAnnotation(GuiceModules.class);
        if (annotation == null) {
            throw new InitializationError("Failed to find GuiceModules annotation for test class : " + testClass.getName());
        }
        return Arrays.asList(annotation.value());
    }

    private List<Class<? extends TestListener>> getTestListenersFor(Class<?> testClass) throws InitializationError {
        TestListeners annotation = testClass.getAnnotation(TestListeners.class);
        return annotation == null ? Collections.emptyList() : Arrays.asList(annotation.value());
    }

    private List<TestListener> findTestListener(List<Class<? extends TestListener>> classes) throws InitializationError {
        List<TestListener> testListeners = new ArrayList<>(classes.size());
        try {
            for (Class<? extends TestListener> testListener : classes) {
                testListeners.add(this.injector.getInstance(testListener));
            }
        } catch (Exception exception) {
            throw new InitializationError(exception);
        }
        return testListeners;
    }

    @Override
    protected Object createTest() throws Exception {
        Object testInstance = injector.getInstance(getTestClass().getJavaClass());

        testContext.setTestInstance(testInstance);
        for (TestListener testListener : this.testListeners) {
            testListener.prepareTestInstance(testContext);
        }

        return testInstance;
    }

    @Override
    public void run(RunNotifier notifier) {
        try {
            for (TestListener testListener : this.testListeners) {
                testListener.beforeTestClass(testContext);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        RunListenerDelegate runListenerDelegate = new RunListenerDelegate(testListeners, testContext);

        notifier.addListener(runListenerDelegate);

        super.run(notifier);

        notifier.removeListener(runListenerDelegate);
    }
}
