package com.ecs.measure.GUI.Objects;

import com.ecs.measure.GUI.Animation;
import com.ecs.measure.GUI.Containers.Container;
import com.ecs.measure.GUI.Interfaces.MouseEventHandler;

public class Object {
    public int x, y, screenX, screenY, width, height;
    public boolean disabled = false, hidden = false, hovered = false;
    public Container parent = null, firstParent = null;
    public MouseEventHandler onMouseEvent = (mouseX, mouseY) -> {};
    public Animation animation;
    
    public Object(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.screenX = x;
        this.screenY = y;
        this.width = width;
        this.height = height;
    }
    
    public boolean isPointInside(int x, int y) {
        return x >= this.x && x <= this.x + width - 1 && y >= this.y && y <= this.y + height - 1;
    }

    public void update() {
        
    }
    
    public void draw(int mouseX, int mouseY, float partialTicks) {
        
    }
}
