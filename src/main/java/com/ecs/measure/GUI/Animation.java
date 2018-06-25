package com.ecs.measure.GUI;

public class Animation {
    public AnimationFrameHandler frameHandler;
    public long duration, startTime = -1;
    
    public Animation(long duration, AnimationFrameHandler frameHandler) {
        this.duration = duration;
        this.frameHandler = frameHandler;
    }
}
