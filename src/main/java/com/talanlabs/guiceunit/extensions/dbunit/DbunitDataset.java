package com.talanlabs.guiceunit.extensions.dbunit;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DbunitDataset {

    String[] value() default {};

    DbunitOperation beforeType() default DbunitOperation.CLEAN_INSERT;

    /**
     * If empty, use value
     */
    String[] afterValue() default {};

    DbunitOperation afterType() default DbunitOperation.CLEAN_INSERT;

}
