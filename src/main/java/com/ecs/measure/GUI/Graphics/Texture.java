package com.ecs.measure.GUI.Graphics;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;

public class Texture {
    public FloatBuffer buffer;
    public int width, height;
    
    public void setPixel(int x, int y, float r, float g, float b, float a) {
        int index = y * width * 4 + x * 4;
        System.out.println("x = " + x + ", y = " + y + ", index = " + index);
        
        if (index >= 0 && index <= buffer.capacity()) {
            buffer.put(index, r);
            buffer.put(index + 1, g);
            buffer.put(index + 2, b);
            buffer.put(index + 3, a);
        }
        else {
            System.out.println("Out of bounds: " + index);
        }
    }
    
    public Texture(String path) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(path));
            
            width = bufferedImage.getWidth();
            height = bufferedImage.getHeight();
            buffer = BufferUtils.createFloatBuffer(width * height * 4);

            int index = 0, color;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    color = bufferedImage.getRGB(x, y);
                    
                    buffer.put(index, (color >> 16 & 0xFF) / 255.0f); index++;
                    buffer.put(index, (color >> 8 & 0xFF) / 255.0f); index++;
                    buffer.put(index, (color & 0xFF) / 255.0f); index++;
                    buffer.put(index, (color >> 24 & 0xFF) / 255.0f); index++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}