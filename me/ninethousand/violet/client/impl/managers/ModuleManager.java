/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.InputEvent$KeyInputEvent
 */
package me.ninethousand.violet.client.impl.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import me.ninethousand.violet.client.api.manager.Manager;
import me.ninethousand.violet.client.api.module.Module;
import me.ninethousand.violet.client.impl.modules.client.BetterChat;
import me.ninethousand.violet.client.impl.modules.client.ClickGui;
import me.ninethousand.violet.client.impl.modules.client.CustomFont;
import me.ninethousand.violet.client.impl.modules.client.Translate;
import me.ninethousand.violet.client.impl.modules.combat.AutoArmor;
import me.ninethousand.violet.client.impl.modules.combat.AutoCrystal;
import me.ninethousand.violet.client.impl.modules.combat.AutoTotem;
import me.ninethousand.violet.client.impl.modules.combat.FastXP;
import me.ninethousand.violet.client.impl.modules.combat.PistonAura;
import me.ninethousand.violet.client.impl.modules.movement.Booster;
import me.ninethousand.violet.client.impl.modules.movement.ReverseStep;
import me.ninethousand.violet.client.impl.modules.movement.Sprint;
import me.ninethousand.violet.client.impl.modules.player.AutoDuper;
import me.ninethousand.violet.client.impl.modules.player.AutoMessage;
import me.ninethousand.violet.client.impl.modules.player.FakePlayer;
import me.ninethousand.violet.client.impl.modules.player.MiddleClick;
import me.ninethousand.violet.client.impl.modules.player.PacketMine;
import me.ninethousand.violet.client.impl.modules.player.Velocity;
import me.ninethousand.violet.client.impl.modules.render.BlockHighlight;
import me.ninethousand.violet.client.impl.modules.render.BurrowESP;
import me.ninethousand.violet.client.impl.modules.render.Chams;
import me.ninethousand.violet.client.impl.modules.render.ChingChongHat;
import me.ninethousand.violet.client.impl.modules.render.CubeCrystals;
import me.ninethousand.violet.client.impl.modules.render.GuiBlur;
import me.ninethousand.violet.client.impl.modules.render.NoRender;
import me.ninethousand.violet.client.impl.modules.render.ParticleEffects;
import me.ninethousand.violet.client.impl.modules.render.PopChams;
import me.ninethousand.violet.client.impl.modules.render.ShaderESP;
import me.ninethousand.violet.client.impl.modules.render.Tooltips;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class ModuleManager
implements Manager {
    private final List<Module> modules;
    private static ModuleManager instance;

    private ModuleManager() {
        ArrayList<Module> list = new ArrayList<Module>();
        list.addAll(Arrays.asList(ClickGui.getInstance(), BetterChat.getInstance(), CustomFont.getInstance(), Translate.getInstance(), AutoCrystal.getInstance(), FastXP.getInstance(), AutoArmor.getInstance(), AutoTotem.getInstance(), PistonAura.getInstance(), Booster.getInstance(), ReverseStep.getInstance(), Sprint.getInstance(), FakePlayer.getInstance(), AutoDuper.getInstance(), AutoMessage.getInstance(), MiddleClick.getInstance(), Velocity.getInstance(), PacketMine.getInstance(), NoRender.getInstance(), Tooltips.getInstance(), ShaderESP.getInstance(), ParticleEffects.getInstance(), PopChams.getInstance(), Chams.getInstance(), GuiBlur.getInstance(), CubeCrystals.getInstance(), BlockHighlight.getInstance(), ChingChongHat.getInstance(), BurrowESP.getInstance()));
        this.modules = list.stream().sorted(ModuleManager::order).collect(Collectors.toList());
        MinecraftForge.EVENT_BUS.register((Object)this);
    }

    private static int order(Module m1, Module m2) {
        return m1.getName().compareTo(m2.getName());
    }

    public List<Module> getModules() {
        return this.modules;
    }

    @SubscribeEvent
    public void listen(InputEvent.KeyInputEvent event) {
        for (Module m : this.modules) {
            if (!m.getBind().getOutput()) continue;
            m.setEnabled(!m.isEnabled());
        }
    }

    public static ModuleManager get() {
        if (instance == null) {
            instance = new ModuleManager();
        }
        return instance;
    }
}

