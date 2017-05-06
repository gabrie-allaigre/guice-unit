package com.talanlabs.guiceunit;

import com.google.inject.Module;

import java.lang.annotation.*;

/**
 * Defines the Guice Modules in use in the test class.
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GuiceModules {

    /**
     * The Guice Modules classes needed by the class under test.
     */
    Class<? extends Module>[] value();

}