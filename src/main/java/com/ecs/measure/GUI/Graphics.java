package com.ecs.measure.GUI;
import net.minecraft.client.Minecraft;

import static org.lwjgl.opengl.GL11.*;

public class Graphics {
    public static void drawRectangle(int x, int y, int width, int height, Color color) {
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBegin(GL_QUADS);
            glColor4d(color.r, color.g, color.b, color.a);
            glVertex2i(x, y);
            glVertex2i(x, y + height);
            glVertex2i(x + width, y + height);
            glVertex2i(x + width, y);
        glEnd();
    }
    
    public static void drawCircle(int centerX, int centerY, int radius, int segments, Color color) {
        double angle = -Math.PI * 2 / segments;
        double sin = Math.sin(angle), cos = Math.cos(angle);
        double x = radius, y = 0, t;
        
        glPushMatrix();
            glDisable(GL_TEXTURE_2D);
            glEnable(GL_BLEND);
            glColor4d(color.r, color.g, color.b, color.a);
            glBegin(GL_TRIANGLE_FAN);
                for (int i = 0; i < segments; i++) {
                    glVertex2d(centerX + x, centerY + y);
        
                    t = x;
                    x = cos * x - sin * y;
                    y = sin * t + cos * y;
                }
            glEnd();
        glPopMatrix();
    }
    
    public static void drawText(int x, int y, Color color, String text, boolean dropShadow) {
        glEnable(GL_TEXTURE_2D);
        glColor4d(color.r, color.g, color.b, color.a);
        Minecraft.getMinecraft().fontRenderer.drawString(text, x, y, 0xFFFFFF, dropShadow);
    }
}
