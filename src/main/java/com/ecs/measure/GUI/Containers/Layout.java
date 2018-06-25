package com.ecs.measure.GUI.Containers;

import com.ecs.measure.GUI.Container;
import com.ecs.measure.GUI.Object;

public class Layout extends Container {
    public int spacing = 4;
    
    public Layout(int x, int y, int width, int height) {
        super(x, y, width, height);
    }
    
    @Override
    public void update() {
        int totalHeight = 0;
        for (Object child : children) {
            if (!child.hidden) {
                totalHeight += child.height + spacing;
            }
        }
        totalHeight -= spacing;

        int y = this.y + this.height / 2 - totalHeight / 2;
        
        for (Object child : children) {
            if (!child.hidden) {
                child.x = this.width / 2 - child.width / 2;
                child.y = y;
            }
            
            y += child.height + spacing;
        }

        super.update();
    }
}
