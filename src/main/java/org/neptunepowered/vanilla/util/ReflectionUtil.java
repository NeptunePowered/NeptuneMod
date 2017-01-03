/*
 * This file is part of NeptuneVanilla, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2015-2017, Jamie Mansfield <https://github.com/jamierocks>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.neptunepowered.vanilla.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A utility for using Reflection.
 */
public final class ReflectionUtil {

    /**
     * Sets the value of a <code>static final</code> field in the given class.
     *
     * @param clazz The class
     * @param field The name of the field
     * @param object The new value
     * @throws Exception If something goes wrong
     */
    public static void setStaticFinalField(Class clazz, String field, Object object) throws Exception {
        try {
            Field instanceField = clazz.getDeclaredField(field);
            instanceField.setAccessible(true);

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(instanceField, instanceField.getModifiers() & ~Modifier.FINAL);

            instanceField.set(null, object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new Exception("Failed to set field: " + field + " in class: " + clazz.getName(), e);
        }
    }

    /**
     * Gets a {@link List} of all the {@link Method}s in the given class, annotated with the given annotation class.
     *
     * @param clazz The class
     * @param annotClazz The annotation class
     * @return The list of methods
     */
    public static List<Method> getMethodsAnnotatedWith(Class<?> clazz, Class<? extends Annotation> annotClazz) {
        return Stream.of(clazz.getDeclaredMethods()).filter(method ->
                Stream.of(method.getDeclaredAnnotations()).anyMatch(annotation ->
                        annotation.annotationType().equals(annotClazz)
                )
        ).collect(Collectors.toList());
    }

    private ReflectionUtil() {
    }

}
