/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package me.ninethousand.violet.client.api.module;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import me.ninethousand.violet.client.api.setting.Setting;
import me.ninethousand.violet.client.api.setting.SettingContainer;
import me.ninethousand.violet.client.impl.managers.EventManager;
import me.ninethousand.violet.client.impl.managers.MessageManager;
import me.ninethousand.violet.client.util.minecraft.MessageBuilder;
import me.ninethousand.violet.client.util.misc.Bind;

public abstract class Module {
    protected final String name;
    protected final String description;
    protected final String id;
    protected final Category category;
    protected String info = "";
    protected boolean enabled = false;
    protected boolean alwaysEnabled = false;
    protected boolean open = false;
    @Setting
    protected Bind keybind;
    protected boolean hidden = false;
    protected List<SettingContainer<?>> settingContainers;

    protected Module() {
        Manifest manifest = this.getClass().getAnnotation(Manifest.class);
        if (manifest == null) {
            throw new NullPointerException();
        }
        this.category = manifest.value();
        this.name = !manifest.name().isEmpty() ? manifest.name() : this.getClass().getSimpleName();
        this.alwaysEnabled = manifest.alwaysEnabled();
        this.enabled = manifest.enabledByDefault() || this.alwaysEnabled;
        this.description = manifest.description();
        this.id = this.getClass().getSimpleName().toLowerCase();
        this.keybind = new Bind(manifest.key());
        if (this.enabled) {
            EventManager.get().register(this);
        }
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled || !enabled && this.alwaysEnabled) {
            return;
        }
        this.enabled = enabled;
        if (enabled) {
            EventManager.get().register(this);
            this.onEnable();
            String toggleMessage = new MessageBuilder().text(this.name + " enabled").format(MessageBuilder.createFormat(ChatFormatting.GREEN)).append().getAsString();
            MessageManager.get().chatNotify(toggleMessage, this.hashCode());
        } else {
            EventManager.get().unregister(this);
            this.onDisable();
            String toggleMessage = new MessageBuilder().text(this.name + " disabled").format(MessageBuilder.createFormat(ChatFormatting.RED)).append().getAsString();
            MessageManager.get().chatNotify(toggleMessage, this.hashCode());
        }
    }

    public boolean toggle() {
        this.setEnabled(!this.isEnabled());
        return this.isEnabled();
    }

    public boolean toggleOpen() {
        this.setOpen(!this.isOpen());
        return this.isOpen();
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getId() {
        return this.id;
    }

    public Category getCategory() {
        return this.category;
    }

    public String getInfo() {
        return this.info;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isAlwaysEnabled() {
        return this.alwaysEnabled;
    }

    public boolean isOpen() {
        return this.open;
    }

    public Module setOpen(boolean open) {
        this.open = open;
        return this;
    }

    public Bind getBind() {
        return this.keybind;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public List<SettingContainer<?>> getSettingContainers() {
        if (this.settingContainers == null) {
            this.settingContainers = SettingContainer.getContainersForObject(this);
        }
        return this.settingContainers;
    }

    protected void onEnable() {
    }

    protected void onDisable() {
    }

    protected void chat(String message) {
        MessageBuilder messageBuilder = new MessageBuilder().text("[" + this.name + "] ").format(MessageBuilder.createFormat(ChatFormatting.LIGHT_PURPLE)).append().text(message).format(MessageBuilder.createFormat(new ChatFormatting[0])).append();
        MessageManager.get().chatNotify(messageBuilder.getAsString(), this.hashCode());
    }

    protected void chat(String message, int id) {
        MessageBuilder messageBuilder = new MessageBuilder().text("[" + this.name + "] ").format(MessageBuilder.createFormat(ChatFormatting.LIGHT_PURPLE)).append().text(message).format(MessageBuilder.createFormat(new ChatFormatting[0])).append();
        MessageManager.get().chatNotify(messageBuilder.getAsString(), id);
    }

    @Retention(value=RetentionPolicy.RUNTIME)
    @Target(value={ElementType.TYPE})
    public static @interface Manifest {
        public Category value();

        public String name() default "";

        public boolean enabledByDefault() default false;

        public boolean alwaysEnabled() default false;

        public int key() default 0;

        public String description() default "";
    }

    public static enum Category {
        COMBAT,
        MOVEMENT,
        PLAYER,
        RENDER,
        CLIENT;

        private boolean open = true;

        public boolean isOpen() {
            return this.open;
        }

        public void setOpen(boolean open) {
            this.open = open;
        }

        public boolean toggleOpen() {
            this.setOpen(!this.isOpen());
            return this.isOpen();
        }
    }
}

