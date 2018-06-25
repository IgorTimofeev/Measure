package com.ecs.measure.Handlers;

import com.ecs.measure.Measure;
import com.ecs.measure.GUI.Examples.MeasureMenuGuiScreen;
import com.ecs.measure.Renderers.MeasureRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;
import java.util.Map;

public class KeyInputHandler {
    private static HashMap<String, KeyBinding> keyBindings = new HashMap<String, KeyBinding>();
    
    private void addKeyBinding(String name, int key) {
        keyBindings.put(name, new KeyBinding(name, key, "keybindingCategory"));
    }
    
    public KeyInputHandler() {
        addKeyBinding("addPin", Keyboard.KEY_V);
        addKeyBinding("openMenu", Keyboard.KEY_C);
        
        for (Map.Entry<String, KeyBinding> entry : keyBindings.entrySet()) {
            ClientRegistry.registerKeyBinding(entry.getValue());
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onKeyPressed(InputEvent.KeyInputEvent event) {
        if (keyBindings.get("addPin").isPressed()) {
            MeasureRenderer.addLookingAt();
        }
        else if (keyBindings.get("openMenu").isPressed()) {
            Measure.minecraft.displayGuiScreen(new MeasureMenuGuiScreen());
        }
    }
}