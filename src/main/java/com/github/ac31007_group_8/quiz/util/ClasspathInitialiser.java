package com.github.ac31007_group_8.quiz.util;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;

/**
 * Finds and executes marked methods.
 *
 * @author Robert T.
 */
public class ClasspathInitialiser {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClasspathInitialiser.class);

    private final Class<? extends Annotation> annotation;
    private final String pkg;

    public ClasspathInitialiser(Class<? extends Annotation> annotation, String pkg) {
        this.annotation = annotation;
        this.pkg = pkg;
    }

    public void callAllMethods() {
        Reflections ref = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(pkg))
                .setScanners(new MethodAnnotationsScanner())
                .filterInputsBy(new FilterBuilder().includePackage(pkg))
        );
        Set<Method> markedMethods = ref.getMethodsAnnotatedWith(annotation);
        markedMethods.forEach((m) -> {
            try {
                int mods = m.getModifiers();
                if (!Modifier.isStatic(mods)) {
                    LOGGER.warn("Method {}#{} is marked with an init annotation, but isn't static! Skipping.", m.getDeclaringClass(), m.getName());
                    return;
                }
                if (m.getParameterCount() != 0) {
                    LOGGER.warn("Method {}#{} is marked with an init annotation, but has parameters! Skipping.", m.getDeclaringClass(), m.getName());
                    return;
                }
                m.invoke(null);
            } catch (Exception ex) {
                LOGGER.warn("Exception when executing {}#{} during init.", m.getDeclaringClass(), m.getName());
                LOGGER.warn(ex.getMessage(), ex);
            }
        });
    }

}
