package com.ecs.measure.Handlers;

import com.ecs.measure.Measure;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class SoundHandler {
    public static final SoundEvent
        PIN_PLACED = register("pinplaced"),
        PIN_CLEARED = register("pincleared");
    
    private static SoundEvent register(String name) {
        final ResourceLocation resourceLocation = new ResourceLocation(Measure.MODID, name);
        return new SoundEvent(resourceLocation).setRegistryName(resourceLocation);
    }
}