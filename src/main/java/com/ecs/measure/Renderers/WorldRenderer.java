package com.ecs.measure.Renderers;

import com.ecs.measure.Measure;
import com.ecs.measure.GUI.Graphics.Color;

import static org.lwjgl.opengl.GL11.*;

public class WorldRenderer {
    static void renderBoxRegularLines(double x1, double y1, double z1, double x2, double y2, double z2, float width, Color color) {
        // Regular lines
        glLineWidth(width);
        glBegin(GL_LINE_STRIP);
            glColor4d(color.r, color.g, color.b, color.a);
    
            // Regular Line Front
            glVertex3d(x1, y1, z1);
            glVertex3d(x1, y2, z1);
            glVertex3d(x2, y2, z1);
            glVertex3d(x2, y1, z1);
            glVertex3d(x1, y1, z1);
    
            // Regular Line Back
            glVertex3d(x1, y1, z2);
            glVertex3d(x1, y2, z2);
            glVertex3d(x2, y2, z2);
            glVertex3d(x2, y1, z2);
            glVertex3d(x1, y1, z2);
    
            // Regular Line Top left
            glVertex3d(x1, y2, z2);
            glVertex3d(x1, y2, z1);
    
            // Regular Line top right
            glVertex3d(x2, y2, z1);
            glVertex3d(x2, y2, z2);
    
            // Regular Line bottom right
            glVertex3d(x2, y1, z2);
            glVertex3d(x2, y1, z1);
        glEnd();
    }
    
    static void renderBoxHelperLines(double x1, double y1, double z1, double x2, double y2, double z2, double stepPercent, float width, Color color) {
        // Helper lines
        glLineWidth(width);
        glColor4d(color.r, color.g, color.b, color.a);
        
        // Vertical
        // Front and back
        double step = (x2 - x1) * stepPercent;
        for (double x = x1 + step; x < x2; x += step) {
            glBegin(GL_LINES);
            glVertex3d(x, y1, z1);
            glVertex3d(x, y2, z1);
            glEnd();

            glBegin(GL_LINES);
            glVertex3d(x, y1, z2);
            glVertex3d(x, y2, z2);
            glEnd();
        }

        // Left and right
        step = (z2 - z1) * stepPercent;
        for (double z = z1 + step; z < z2; z += step) {
            glBegin(GL_LINES);
            glVertex3d(x1, y1, z);
            glVertex3d(x1, y2, z);
            glEnd();

            glBegin(GL_LINES);
            glVertex3d(x2, y1, z);
            glVertex3d(x2, y2, z);
            glEnd();
        }

        // Horizontal
        step = (y2 - y1) * stepPercent;
        for (double y = y1 + step; y < y2; y += step) {
            // Front and back
            glBegin(GL_LINES);
            glVertex3d(x1, y, z1);
            glVertex3d(x2, y, z1);
            glEnd();

            glBegin(GL_LINES);
            glVertex3d(x1, y, z2);
            glVertex3d(x2, y, z2);
            glEnd();

            // Left and right
            glBegin(GL_LINES);
            glVertex3d(x1, y, z1);
            glVertex3d(x1, y, z2);
            glEnd();

            // Left and right
            glBegin(GL_LINES);
            glVertex3d(x2, y, z1);
            glVertex3d(x2, y, z2);
            glEnd();
        }
    }
    
    static void renderBox(double x1, double y1, double z1, double x2, double y2, double z2, Color color) {
        // Background
        glBegin(GL_QUADS);
        glColor4d(color.r, color.g, color.b, color.a);

        // Background Front
        glVertex3d(x1, y1, z1);
        glVertex3d(x1, y2, z1);
        glVertex3d(x2, y2, z1);
        glVertex3d(x2, y1, z1);

        // Background Back
        glVertex3d(x1, y1, z2);
        glVertex3d(x1, y2, z2);
        glVertex3d(x2, y2, z2);
        glVertex3d(x2, y1, z2);

        // Background Left
        glVertex3d(x1, y1, z1);
        glVertex3d(x1, y2, z1);
        glVertex3d(x1, y2, z2);
        glVertex3d(x1, y1, z2);

        // Background Right
        glVertex3d(x2, y1, z1);
        glVertex3d(x2, y2, z1);
        glVertex3d(x2, y2, z2);
        glVertex3d(x2, y1, z2);

        // Background Bottom
        glVertex3d(x1, y1, z1);
        glVertex3d(x1, y1, z2);
        glVertex3d(x2, y1, z2);
        glVertex3d(x2, y1, z1);

        // Background Top
        glVertex3d(x1, y2, z1);
        glVertex3d(x1, y2, z2);
        glVertex3d(x2, y2, z2);
        glVertex3d(x2, y2, z1);

        glEnd();
    }

    public static void renderCenteredText(double x, double y, double z, double yawRotation, double scale, boolean shadow, int color, String text) {
        glPushMatrix();
            glTranslated(x, y, z);
            glRotatef(180, 1, 0, 0);
            glRotated(yawRotation + 180,0, 1, 0);
            glScaled(scale, scale, scale);

            int textWidth = Measure.minecraft.fontRenderer.getStringWidth(text);
            Measure.minecraft.fontRenderer.drawString(text, -textWidth / 2, 0, color, shadow);
        glPopMatrix();
    }
}
