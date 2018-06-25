package com.ecs.measure.GUI;
import static org.lwjgl.opengl.GL11.*;

public class Graphics {
    public static void drawRectangle(int x, int y, int width, int height, Color color) {
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);

        glBegin(GL_QUADS);
            glColor4f(color.r, color.g, color.b, color.a);
            glVertex2i(x, y);
            glVertex2i(x, y + height - 1);
            glVertex2i(x + width - 1, y + height - 1);
            glVertex2i(x + width - 1, y);
        glEnd();
    }
}
