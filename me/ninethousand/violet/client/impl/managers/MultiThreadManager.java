/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.impl.managers;

import java.util.ArrayDeque;
import java.util.Queue;

public class MultiThreadManager {
    private final Queue<Runnable> tasks = new ArrayDeque<Runnable>();
    private final Thread thread = new Thread(){

        @Override
        public void run() {
            while (!this.isInterrupted()) {
                try {
                    Thread.sleep(100L);
                    while (!MultiThreadManager.this.tasks.isEmpty()) {
                        MultiThreadManager.this.executeNextTask();
                    }
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private static MultiThreadManager instance;

    private MultiThreadManager() {
    }

    public synchronized void addTask(Runnable task) {
        this.tasks.add(task);
    }

    public synchronized void removeTask(Runnable task) {
        this.tasks.remove(task);
    }

    public synchronized void clearTasks() {
        this.tasks.clear();
    }

    private synchronized void executeNextTask() {
        Runnable nextTask = this.tasks.poll();
        if (nextTask != null) {
            nextTask.run();
        }
    }

    public void startThread() {
        this.thread.start();
    }

    public static MultiThreadManager get() {
        if (instance == null) {
            instance = new MultiThreadManager();
        }
        return instance;
    }
}

