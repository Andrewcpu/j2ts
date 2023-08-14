package net.andrewcpu.j2ts.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD) // This annotation can be used on methods.
public @interface API {
    String description() default ""; // Add this line.
    String returnValue() default "";

    boolean isMultipart() default false;
}
