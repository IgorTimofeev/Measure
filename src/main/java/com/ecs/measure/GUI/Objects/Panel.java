package com.ecs.measure.GUI.Objects;

import com.ecs.measure.GUI.Graphics.Renderer;
import com.ecs.measure.GUI.Graphics.Color;
import com.ecs.measure.GUI.Object;

public class Panel extends Object {
    public Color color;
    
    public Panel(int x, int y, int width, int height, Color color) {
        super(x, y, width, height);
        this.color = color;
    }

    @Override
    public void update() {
        
    }

    @Override
    public void draw() {
        Renderer.drawRectangle(screenX, screenY, width, height, color);
    }
}
