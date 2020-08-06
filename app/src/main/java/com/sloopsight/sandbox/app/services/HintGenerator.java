package com.sloopsight.sandbox.app.services;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.springframework.stereotype.Component;

import com.sloopsight.sandbox.app.meta.Intellisense;
import com.sloopsight.sandbox.app.meta.MethodHint;
import com.sloopsight.sandbox.app.meta.ParamHint;

@Component
public class HintGenerator {


    public List<String> get() {

        List<String> hints = new LinkedList<>();

        List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("com.sloopsight.sandbox.app.services"))));

        Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);

        classes.forEach(c -> {
            if (c.isAnnotationPresent(Intellisense.class)) {
                Intellisense intellisense = null;
                for (Annotation annotation : c.getAnnotations()) {
                    if (annotation instanceof Intellisense) {
                        intellisense = (Intellisense) annotation;
                        break;

                    }
                }
                if (intellisense != null) {

                    for (Method m : c.getMethods()) {
                        if (m.isAnnotationPresent(MethodHint.class)) {
                            MethodHint methodHint = m.getAnnotation(MethodHint.class);
                            String argument = "";
                            for (Parameter parameter : m.getParameters()) {
                                ParamHint paramHint = parameter.getAnnotation(ParamHint.class);
                                if (paramHint != null) {
                                    argument += "/*" + paramHint.value() + "*/,";
                                }
                            }
                            if (argument.endsWith(",")) {
                                argument = argument.substring(0, argument.length() - 1);
                            }
                            hints.add(intellisense.value() + "." + methodHint.name() + "(" + argument + "); //"
                                    + methodHint.comment());
                        }
                    }
                }
            }

        });
        return hints;
    }
}
