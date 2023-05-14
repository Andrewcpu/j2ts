package net.andrewcpu.j2ts.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER) // This annotation can be used on method parameters
public @interface ParamDescription {
    String value() default ""; // The description of the parameter
}
