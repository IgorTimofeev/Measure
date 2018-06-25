package com.ecs.measure.GUI.Objects;

import com.ecs.measure.GUI.Graphics;
import com.ecs.measure.GUI.Object;
import net.minecraft.util.ResourceLocation;

public class Image extends Object {
    public ResourceLocation texture;

    public Image(int x, int y, int width, int height, ResourceLocation texture) {
        super(x, y, width, height);
        this.texture = texture;
    }

    @Override
    public void update() {
        
    }
    
    @Override
    public void draw() {
        Graphics.drawImage(screenX, screenY, width, height, texture);
    }
}