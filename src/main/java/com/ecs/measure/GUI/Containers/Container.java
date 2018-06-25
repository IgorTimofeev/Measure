package com.ecs.measure.GUI.Containers;

import com.ecs.measure.GUI.Objects.Object;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;

public class Container extends Object {
    public ArrayList<Object> children = new ArrayList<>();
    
    public Container(int x, int y, int width, int height) {
        super(x, y, width, height);
    }
    
    private void updateFirstParent(Object object, Container firstParent) {
        object.firstParent = firstParent;
        if (object instanceof Container) {
            Container container = (Container) object;
            for (int i = 0; i < container.children.size(); i++) {
                updateFirstParent(container.children.get(i), firstParent);
            }
        }
    }
    
    private void doAddChild(Object child) {
        child.parent = this;
        updateFirstParent(child, this.firstParent == null ? this : this.firstParent);
    }
    
    public void addChild(Object child) {
        this.children.add(child);
        doAddChild(child);
    }

    public void addChild(Object child, int atIndex) {
        this.children.add(atIndex, child);
        doAddChild(child);
    }

    @Override
    public void update() {
        Object child;
        for (int i = 0; i < children.size(); i++) {
            child = children.get(i);
            
            if (!child.hidden) {
                if (child.animation != null) {
                    float position = (float) (Minecraft.getSystemTime() - child.animation.startTime) / (float) child.animation.duration;
                    if (position > 1) {
                        child.animation.frameHandler.run(1);
                        child.animation = null;
                    }
                    else {
                        child.animation.frameHandler.run(position);
                    }
                }

                child.screenX = this.x + child.x;
                child.screenY = this.y + child.y;
                child.update();
            }   
        }
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        for (Object child : children) {
            if (!child.hidden) {
                child.draw(mouseX, mouseY, partialTicks);
            }
        }
    }
}
