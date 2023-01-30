/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.util.misc;

import java.util.List;

public class ListUtils {
    public static <T> int getPrevIndex(List<T> list, int currentIndex) {
        int previousIndex = currentIndex - 1;
        if (previousIndex < 0) {
            previousIndex = list.size() - 1;
        }
        return previousIndex;
    }

    public static <T> int getNextIndex(List<T> list, int currentIndex) {
        int nextIndex = currentIndex + 1;
        if (nextIndex >= list.size()) {
            nextIndex = 0;
        }
        return nextIndex;
    }

    private ListUtils() {
        throw new UnsupportedOperationException();
    }
}

