package com.ecs.measure.GUI.Containers;

public class FittedLayout extends Layout {
    public FittedLayout(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void update() {
        this.width = this.parent.width;
        this.height = this.parent.height;
        super.update();
    }
}