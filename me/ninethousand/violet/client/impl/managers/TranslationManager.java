/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.impl.managers;

import me.ninethousand.violet.client.api.manager.Manager;
import me.ninethousand.violet.client.util.translation.Translator;

public class TranslationManager
implements Manager {
    private final Translator translator = new Translator();
    private static TranslationManager instance;

    private TranslationManager() {
    }

    public Translator getTranslator() {
        return this.translator;
    }

    public static TranslationManager get() {
        if (instance == null) {
            instance = new TranslationManager();
        }
        return instance;
    }
}

