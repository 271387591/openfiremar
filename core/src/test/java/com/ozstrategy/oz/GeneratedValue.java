package com.ozstrategy.oz;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(java.lang.annotation.ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GeneratedValue {

	public abstract GenerationType strategy() default GenerationType.IDENTITY;

	public abstract String sequenceName() default "";
}
