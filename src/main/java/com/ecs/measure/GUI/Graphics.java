package com.ecs.measure.GUI;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import static org.lwjgl.opengl.GL11.*;

public class Graphics {
    public static void drawRectangle(int x, int y, int width, int height, Color color) {
        glPushMatrix();
            glDisable(GL_TEXTURE_2D);
            glEnable(GL_BLEND);
            glTranslated(x, y, 0);
            
            glBegin(GL_QUADS);
                glColor4d(color.r, color.g, color.b, color.a);
                glVertex2i(0, 0);
                glVertex2i(0, height);
                glVertex2i(width, height);
                glVertex2i(width, 0);
            glEnd();
        glPopMatrix();
    }
    
    public static void drawCircle(int centerX, int centerY, int radius, int segments, Color color) {
        double angle = Math.PI * 2 / segments;
        double sin = Math.sin(angle), cos = Math.cos(angle);
        double x = radius, y = 0, t;
        
        glPushMatrix();
            glDisable(GL_TEXTURE_2D);
            glEnable(GL_BLEND);
            glDisable(GL_CULL_FACE);
            glTranslated(centerX, centerY, 0);
                
            glBegin(GL_TRIANGLE_FAN);
                glColor4d(color.r, color.g, color.b, color.a);
                
                for (int i = 0; i < segments; i++) {
                    glVertex2d(x, y);
        
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
    
    public static void drawImage(int x, int y, int width, int height, ResourceLocation resourceLocation) {
        glPushMatrix();
            glEnable(GL_TEXTURE_2D);
            glDisable(GL_CULL_FACE);
            glTranslated(x, y, 0);
            Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
            
            glColor3f(1, 1, 1);
            glBegin(GL_QUADS);
                glTexCoord2f(0, 0);
                glVertex2i(0, 0);
        
                glTexCoord2f(1, 0);
                glVertex2i(width, 0);
        
                glTexCoord2f(1, 1);
                glVertex2i(width, height);
        
                glTexCoord2f(0, 1);
                glVertex2i(0, height);
            glEnd();
        glPopMatrix();
    }
}
