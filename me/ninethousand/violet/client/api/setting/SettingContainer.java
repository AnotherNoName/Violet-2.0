/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.util.internal.ReflectionUtil
 */
package me.ninethousand.violet.client.api.setting;

import io.netty.util.internal.ReflectionUtil;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.api.setting.Setting;

public class SettingContainer<T> {
    private static Setting annotation;
    private final Field field;
    private final String name;
    private final String description;
    private T currentValue;
    private final Object host;
    private final String category;
    private final double min;
    private final double max;
    private final int dp;
    private boolean open = false;
    private boolean typing = false;

    private SettingContainer(Field field, T currentValue, Object host, String category) {
        Setting setting = field.getDeclaredAnnotation(Setting.class);
        this.field = field;
        this.field.setAccessible(true);
        this.name = !setting.name().equals("") ? setting.name() : SettingContainer.formatName(field.getName());
        this.description = !setting.description().equals("") ? setting.description() : "No description.";
        this.currentValue = currentValue;
        this.host = host;
        this.category = category;
        this.min = setting.min();
        this.max = setting.max();
        this.dp = setting.dp();
    }

    public T getValue() {
        return this.currentValue;
    }

    public void setValue(T value) {
        try {
            this.currentValue = value;
            this.field.set(this.host, this.currentValue);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getCategory() {
        return this.category;
    }

    public double getMin() {
        return this.min;
    }

    public double getMax() {
        return this.max;
    }

    public int getDp() {
        return this.dp;
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isTyping() {
        return this.typing;
    }

    public void setTyping(boolean typing) {
        this.typing = typing;
    }

    public Module getModule() {
        if (this.host instanceof Module) {
            return (Module)this.host;
        }
        return null;
    }

    public static List<SettingContainer<?>> getContainersForObject(Object obj) {
        Setting annotation;
        Throwable error;
        ArrayList containers = new ArrayList();
        for (Field field : obj.getClass().getSuperclass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(Setting.class)) continue;
            if (Modifier.isPublic(field.getModifiers())) {
                throw new IllegalStateException("Fields annotated with Setting must not be accessible!");
            }
            error = ReflectionUtil.trySetAccessible((AccessibleObject)field);
            if (error != null) {
                throw new IllegalStateException("Could not make field " + field.getName() + " accessible.");
            }
            if (Modifier.isFinal(field.getModifiers())) {
                throw new IllegalStateException("Fields annotated with Setting must not be final!");
            }
            annotation = field.getAnnotation(Setting.class);
            try {
                containers.add(new SettingContainer<Object>(field, field.get(obj), obj, annotation.category()));
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(Setting.class)) continue;
            if (Modifier.isPublic(field.getModifiers())) {
                throw new IllegalStateException("Fields annotated with Setting must not be accessible!");
            }
            error = ReflectionUtil.trySetAccessible((AccessibleObject)field);
            if (error != null) {
                throw new IllegalStateException("Could not make field " + field.getName() + " accessible.");
            }
            if (Modifier.isFinal(field.getModifiers())) {
                throw new IllegalStateException("Fields annotated with Setting must not be final!");
            }
            annotation = field.getAnnotation(Setting.class);
            try {
                containers.add(new SettingContainer<Object>(field, field.get(obj), obj, annotation.category()));
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return containers;
    }

    private static String formatName(String name) {
        StringBuilder result = new StringBuilder();
        for (char c : name.toCharArray()) {
            boolean isUpperCase = String.valueOf(c).equals(String.valueOf(c).toUpperCase());
            if (isUpperCase) {
                result.append(" ");
            }
            result.append(c);
        }
        return result.toString().replaceFirst(String.valueOf(result.charAt(0)), String.valueOf(result.charAt(0)).toUpperCase());
    }
}

