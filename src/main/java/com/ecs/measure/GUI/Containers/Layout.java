package com.ecs.measure.GUI.Containers;

import com.ecs.measure.GUI.Objects.Object;

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
        totalHeight -= spacing - 50;
        
        int totalWidth = 0;
        for (Object child : children) {
            if (!child.hidden) {
                totalWidth = Math.max(totalWidth, child.width);
            }
        }
        
        int x = this.x + this.width / 2 - totalWidth / 2;
        int y = this.y + this.height / 2 - totalHeight / 2;

//        System.out.println("height = " + height + ", y = " + y + ", totalHegiht = " + totalHeight);
        
        for (Object child : children) {
            if (!child.hidden) {
                child.x = x;
                child.y = y += child.height + spacing;
            }
        }

        super.update();
    }
}
