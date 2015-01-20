package com.ozstrategy.oz;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(java.lang.annotation.ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

	public abstract String name() default "";

	public abstract boolean nullable() default true;

}