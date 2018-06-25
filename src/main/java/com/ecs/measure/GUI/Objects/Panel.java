package com.ecs.measure.GUI.Objects;

import com.ecs.measure.GUI.Graphics;
import com.ecs.measure.GUI.Color;

public class Panel extends Object {
    public Color color;
    
    public Panel(int x, int y, int width, int height, Color color) {
        super(x, y, width, height);
        this.color = color;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        Graphics.drawRectangle(screenX, screenY, width, height, color);
    }
}