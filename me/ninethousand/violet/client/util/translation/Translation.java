/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonParser
 */
package me.ninethousand.violet.client.util.translation;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import me.ninethousand.violet.client.util.translation.Language;

public class Translation {
    private final Language source;
    private final Language target;
    private final String original;
    private final String translated;

    public Translation(Language source, Language target, String original, String data) {
        this.source = source;
        this.target = target;
        this.original = original;
        if (data == null) {
            this.translated = "Error Translating";
            return;
        }
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(data).getAsJsonArray().get(0);
        this.translated = element.isJsonArray() ? element.getAsJsonArray().get(0).getAsString() : element.getAsString();
    }

    public Language getSource() {
        return this.source;
    }

    public Language getTarget() {
        return this.target;
    }

    public String getOriginal() {
        return this.original;
    }

    public String getTranslated() {
        return this.translated;
    }
}

