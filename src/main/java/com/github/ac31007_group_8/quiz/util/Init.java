package com.github.ac31007_group_8.quiz.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation for methods which should be called during init. These will be called <b>after</b> configuration
 * has been loaded.
 *
 * @author Robert T.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Init {}
