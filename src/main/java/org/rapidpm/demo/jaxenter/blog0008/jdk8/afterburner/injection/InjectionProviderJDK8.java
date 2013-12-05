package org.rapidpm.demo.jaxenter.blog0008.jdk8.afterburner.injection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

/**
 * Created by Sven Ruppert on 04.12.13.
 */
public class InjectionProviderJDK8 {

    private static Map<Class, Object> models = new HashMap<>();
    private static List<Object> presenters = new ArrayList<>();

    public static Object instantiatePresenter(Class clazz) {
        try {
            return registerExistingAndInject(clazz.newInstance());
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new IllegalStateException("Cannot instantiate view: " + clazz, ex);
        }
    }

    /**
     * Caches the passed presenter internally and injects all fields internally
     *
     * @param instance An already existing (legacy) presenter interesting in
     *                 injection
     * @return presenter with injected fields
     */
    public static Object registerExistingAndInject(Object instance) {
        Object product = injectAndInitialize(instance);
        presenters.add(product);
        return product;
    }

    static Object instantiateModel(Class clazz) {
        Object product = models.get(clazz);
        if (product == null) {
            try {
                product = injectAndInitialize(clazz.newInstance());
                models.put(clazz, product); //kein LifeCycle per Annotation
            } catch (InstantiationException | IllegalAccessException ex) {
                throw new IllegalStateException("Cannot instantiate view: " + clazz, ex);
            }
        }
        return product;
    }

    static Object injectAndInitialize(Object product) {
        injectMembers(product); //rekusives injecten
        initialize(product);    //rekursives init
        return product;
    }

    static void injectMembers(final Object instance) {
        Class<?> aClass = instance.getClass();
        List<Field> fields = Arrays.asList(aClass.getDeclaredFields());
        fields.stream()
                .filter(f -> f.isAnnotationPresent(Inject.class))
                .forEach(field -> {
                    Class<?> type = field.getType();
                    final Object target = instantiateModel(type);
                    AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
                        boolean wasAccessible = field.isAccessible();
                        try {
                            field.setAccessible(true);
                            field.set(instance, target);
                            return null; // return nothing...
                        } catch (IllegalArgumentException | IllegalAccessException ex) {
                            throw new IllegalStateException("Cannot set field: " + field, ex);
                        } finally {
                            field.setAccessible(wasAccessible);
                        }
                    });
                });
    }

    static void initialize(Object instance) {
        invokeMethodWithAnnotation(instance, PostConstruct.class);
    }

    static void destroy(Object instance) {
        invokeMethodWithAnnotation(instance, PreDestroy.class);
    }

    static void invokeMethodWithAnnotation(final Object instance, final Class<? extends Annotation> annotationClass)
            throws IllegalStateException, SecurityException {
        Class<?> aClass = instance.getClass();
        List<Method> declaredMethods = Arrays.asList(aClass.getDeclaredMethods());

        declaredMethods.stream()
                .filter((m) -> m.isAnnotationPresent(annotationClass))
                .forEach((method) -> {
                    AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
                        boolean wasAccessible = method.isAccessible();
                        try {
                            method.setAccessible(true);
                            return method.invoke(instance);
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                            throw new IllegalStateException("Problem invoking " + annotationClass + " : " + method, ex);
                        } finally {
                            method.setAccessible(wasAccessible);
                        }
                    });
                });
    }

    public static void forgetAll() {
        Collection<Object> values = models.values();

        values.forEach(InjectionProviderJDK8::destroy);
        presenters.forEach(InjectionProviderJDK8::destroy);

        presenters.clear();
        models.clear();
    }
}
