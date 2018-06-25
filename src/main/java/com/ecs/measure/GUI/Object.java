package com.ecs.measure.GUI;

public abstract class Object extends Rectangle {
    public int screenX, screenY;
    public boolean disabled = false, hidden = false, hovered = false;
    public Container parent = null, firstParent = null;
    public MouseEventHandler onMouseEvent = null;
    public Animation animation;

    public Object(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.screenX = x;
        this.screenY = y;
    }

    public boolean isPointInside(int x, int y) {
        return x >= this.x && x <= this.x + width - 1 && y >= this.y && y <= this.y + height - 1;
    }

    public abstract void update();
    public abstract void draw();
}
