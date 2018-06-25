package com.ecs.measure.GUI;

public class Rectangle {
    public int x, y, width, height;

    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public static Rectangle getIntersection(Rectangle r1, Rectangle r2) {
        int r1x2 = r1.x + r1.width - 1;
        int r1y2 = r1.y + r1.height - 1;
        int r2x2 = r2.x + r2.width - 1;
        int r2y2 = r2.y + r2.height - 1;
        
        if (r2.x <= r1x2 && r2.y <= r1y2 && r2x2 >= r1.x && r2y2 >= r1.y) {
            int r3x1 = Math.max(r2.x, r1.x);
            int r3y1 = Math.max(r2.y, r1.y);
            
            return new Rectangle(
                r3x1,
                r3y1,
                Math.min(r2x2, r1x2) - r3x1,
                Math.min(r2y2, r1y2) - r3y1
            );
        }
        else {
            return null;
        }
    }
}
