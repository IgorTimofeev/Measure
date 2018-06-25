package com.ecs.measure.GUI;

import com.ecs.measure.GUI.Interfaces.AnimationFrameHandler;
import net.minecraft.client.Minecraft;

public class Animation {
    public AnimationFrameHandler frameHandler;
    public long duration, startTime;
    
    public Animation(long duration, AnimationFrameHandler frameHandler) {
        this.startTime = Minecraft.getSystemTime();
        this.duration = duration;
        this.frameHandler = frameHandler;
    }
}
