package org.jarvis.file.csv;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CSVField {

    String name() default EMPTY;

    int order() default DEFAULT_ORDER;

    boolean ignore() default false;

    /* only for date field*/
    String format() default EMPTY;

    /* for compliance type */
    boolean json() default false;

    Class<?> converter() default void.class;





    /* default value*/

    String EMPTY = "";

    int DEFAULT_ORDER = Integer.MAX_VALUE;
}
