/*
 * Decompiled with CFR 0.152.
 */
package me.ninethousand.violet.client.impl.modules.client;

import java.nio.charset.StandardCharsets;
import me.ninethousand.violet.client.api.event.handler.Listener;
import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.api.setting.Setting;
import me.ninethousand.violet.client.impl.events.ChatEvent;
import me.ninethousand.violet.client.impl.managers.CommandManager;
import me.ninethousand.violet.client.impl.managers.TranslationManager;
import me.ninethousand.violet.client.util.translation.Language;
import me.ninethousand.violet.client.util.translation.Translation;

@Module.Manifest(value=Module.Category.CLIENT, description="Speak multiple languages")
public class Translate
extends Module {
    @Setting
    private boolean incoming = true;
    @Setting(category="Sending")
    private boolean sending = true;
    @Setting(category="Sending")
    private SendMode sendMode = SendMode.Standard;
    @Setting(category="Language")
    private Language myLanguage = Language.ENGLISH;
    @Setting(category="Language")
    private Language theirLanguage = Language.ENGLISH;
    private static Translate instance;

    @Listener
    private void listen(ChatEvent.Incoming event) {
        if (!this.incoming) {
            return;
        }
        Translation translation = TranslationManager.get().getTranslator().translate(event.getMessage(), this.myLanguage);
        event.setMessage(new String(translation.getTranslated().getBytes(), StandardCharsets.UTF_8));
    }

    @Listener
    private void listen(ChatEvent.Outgoing event) {
        if (!this.sending) {
            return;
        }
        switch (this.sendMode) {
            case Shortcut: {
                int i;
                String message = event.getMessage();
                int startIndex = -1;
                int endIndex = -1;
                char[] characters = message.toCharArray();
                for (i = 0; i < characters.length - 3; ++i) {
                    if (characters[i] != '@' || characters[i + 1] != 'T' || characters[i + 2] != '(') continue;
                    startIndex = i + 3;
                    break;
                }
                if (startIndex == -1) break;
                for (i = startIndex; i < characters.length; ++i) {
                    if (characters[i] != ')') continue;
                    endIndex = i;
                    break;
                }
                if (endIndex == -1) break;
                String translate = message.substring(startIndex, endIndex);
                Translation translation1 = TranslationManager.get().getTranslator().translate(translate, this.theirLanguage);
                translate = new String(translation1.getTranslated().getBytes(), StandardCharsets.UTF_8);
                String pre = message.substring(0, startIndex - 3);
                String post = message.substring(endIndex + 1);
                event.setMessage(pre + translate + post);
                break;
            }
            default: {
                if (event.getMessage().startsWith("/") || event.getMessage().startsWith(CommandManager.get().getPrefix())) break;
                Translation translation2 = TranslationManager.get().getTranslator().translate(event.getMessage(), this.theirLanguage);
                event.setMessage(new String(translation2.getTranslated().getBytes(), StandardCharsets.UTF_8));
            }
        }
    }

    public static Translate getInstance() {
        if (instance == null) {
            instance = new Translate();
        }
        return instance;
    }

    private static enum SendMode {
        Standard,
        Shortcut;

    }
}

