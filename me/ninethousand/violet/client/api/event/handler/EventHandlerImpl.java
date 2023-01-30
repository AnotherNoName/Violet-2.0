/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.api.event.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import me.ninethousand.violet.client.api.event.handler.EventHandler;
import me.ninethousand.violet.client.api.event.handler.Listener;

public class EventHandlerImpl
implements EventHandler {
    private final Map<Class<?>, Map<Object, List<Method>>> subscriptionMap = new ConcurrentHashMap();
    private final Map<Class<?>, Map<Class<?>, List<Method>>> syntheticSubscriptionMap = new ConcurrentHashMap();
    private final List<Object> registeredObjectList = new ArrayList<Object>();
    private final List<Class<?>> registeredClassList = new ArrayList();
    private final List<EventHandler> attachedHandlerList = new ArrayList<EventHandler>();

    @Override
    public void register(Object object) {
        if (!this.registeredObjectList.contains(object)) {
            ConcurrentHashMap methodListMap = new ConcurrentHashMap();
            for (Method method2 : object.getClass().getDeclaredMethods()) {
                if (!method2.isAnnotationPresent(Listener.class) || method2.getParameterCount() != 1 || !method2.getReturnType().isAssignableFrom(Void.TYPE) || Modifier.isStatic(method2.getModifiers())) continue;
                methodListMap.putIfAbsent(method2.getParameterTypes()[0], new ArrayList());
                method2.setAccessible(true);
                ((List)methodListMap.get(method2.getParameterTypes()[0])).add(method2);
            }
            for (Map.Entry entry : methodListMap.entrySet()) {
                this.subscriptionMap.putIfAbsent((Class<?>)entry.getKey(), new ConcurrentHashMap());
                entry.setValue(((List)entry.getValue()).stream().sorted(Comparator.comparing(method -> -method.getDeclaredAnnotation(Listener.class).priority())).collect(Collectors.toList()));
                this.subscriptionMap.get(entry.getKey()).put(object, (List<Method>)entry.getValue());
            }
            this.registeredObjectList.add(object);
        }
    }

    @Override
    public void unregister(Object object) {
        if (this.registeredObjectList.contains(object)) {
            for (Class<?> clazz : this.subscriptionMap.keySet()) {
                this.subscriptionMap.get(clazz).remove(object);
            }
            this.registeredObjectList.remove(object);
        }
    }

    @Override
    public void register(Class<?> clazz) {
        if (!this.registeredClassList.contains(clazz)) {
            ConcurrentHashMap methodListMap = new ConcurrentHashMap();
            for (Method method2 : clazz.getDeclaredMethods()) {
                if (!method2.isAnnotationPresent(Listener.class) || method2.getParameterCount() != 1 || !method2.getReturnType().isAssignableFrom(Void.TYPE) || !Modifier.isStatic(method2.getModifiers())) continue;
                methodListMap.putIfAbsent(method2.getParameterTypes()[0], new ArrayList());
                method2.setAccessible(true);
                ((List)methodListMap.get(method2.getParameterTypes()[0])).add(method2);
            }
            for (Map.Entry entry : methodListMap.entrySet()) {
                this.syntheticSubscriptionMap.putIfAbsent((Class<?>)entry.getKey(), new ConcurrentHashMap());
                entry.setValue(((List)entry.getValue()).stream().sorted(Comparator.comparing(method -> -method.getDeclaredAnnotation(Listener.class).priority())).collect(Collectors.toList()));
                this.syntheticSubscriptionMap.get(entry.getKey()).put(clazz, (List<Method>)entry.getValue());
            }
            this.registeredClassList.add(clazz);
        }
    }

    @Override
    public void unregister(Class<?> clazz) {
    }

    @Override
    public boolean isRegistered(Object object) {
        return this.registeredObjectList.contains(object);
    }

    @Override
    public boolean isRegistered(Class<?> clazz) {
        return false;
    }

    @Override
    public void attach(EventHandler handler) {
        if (!this.attachedHandlerList.contains(handler)) {
            this.attachedHandlerList.add(handler);
        }
    }

    @Override
    public void detach(EventHandler handler) {
        this.attachedHandlerList.remove(handler);
    }

    @Override
    public boolean isAttached(EventHandler handler) {
        return this.attachedHandlerList.contains(handler);
    }

    @Override
    public <T> void post(T object) {
        if (this.subscriptionMap.containsKey(object.getClass())) {
            for (Map.Entry<Object, List<Method>> entry : this.subscriptionMap.get(object.getClass()).entrySet()) {
                for (Method method : entry.getValue()) {
                    try {
                        method.invoke(entry.getKey(), object);
                    }
                    catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (this.syntheticSubscriptionMap.containsKey(object.getClass())) {
            for (Map.Entry<Object, List<Method>> entry : this.syntheticSubscriptionMap.get(object.getClass()).entrySet()) {
                for (Method method : entry.getValue()) {
                    try {
                        method.invoke(null, object);
                    }
                    catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (!this.attachedHandlerList.isEmpty()) {
            this.attachedHandlerList.forEach(handler -> handler.post(object));
        }
    }
}

