package com.ecs.measure.GUI.Objects;

import com.ecs.measure.GUI.Graphics.Renderer;
import com.ecs.measure.GUI.Graphics.Texture;
import com.ecs.measure.GUI.Object;

public class Image extends Object {
    public Texture texture;

    public Image(int x, int y, int width, int height, String path) {
        super(x, y, width, height);
        this.texture = new Texture(path);
    }

    @Override
    public void update() {
        
    }
    
    @Override
    public void draw() {
        Renderer.drawImage(screenX, screenY, width, height, texture);
    }
}