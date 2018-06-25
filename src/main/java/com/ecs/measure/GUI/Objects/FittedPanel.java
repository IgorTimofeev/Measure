package com.ecs.measure.GUI.Objects;
import com.ecs.measure.GUI.Color;

public class FittedPanel extends Panel {
    public FittedPanel(int x, int y, int width, int height, Color color) {
        super(x, y, width, height, color);
    }

    @Override
    public void update() {
        this.width = this.parent.width;
        this.height = this.parent.height;
    }
}
