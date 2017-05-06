package com.talanlabs.guiceunit;

import java.lang.annotation.*;

/**
 * Defines the TestListener
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TestListeners {

    /**
     * The TestListener classes needed by the class under test.
     */
    Class<? extends TestListener>[] value();

}