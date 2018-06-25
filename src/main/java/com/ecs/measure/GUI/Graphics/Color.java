package com.ecs.measure.GUI.Graphics;

public class Color {
    public double r, g, b, a;

    public Color(double r, double g, double b, double a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Color(double r, double g, double b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 1;
    }

    public Color(int color) {
        this.a = color > 0xFFFFFF ? (color >> 24 & 0xFF) / 255.0d : 1;
        this.r = (color >> 16 & 0xFF) / 255.0d;
        this.g = (color >> 8 & 0xFF) / 255.0d;
        this.b = (color & 0xFF) / 255.0d;
    }

    public static int toARGB(Color color) {
        return ((int) (color.a * 255) << 24) | ((int) (color.r * 255) << 16) | ((int) (color.g * 255) << 8) | ((int) (color.b * 255));
    }
    
    public static Color transition(Color color1, Color color2, double factor) {
        return new Color(
            color1.r + (color2.r - color1.r) * factor,
            color1.g + (color2.g - color1.g) * factor,
            color1.b + (color2.b - color1.b) * factor,
            color1.a + (color2.a - color1.a) * factor
        );
    }
}