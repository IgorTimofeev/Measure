package com.ecs.measure.GUI.Objects;

import com.ecs.measure.GUI.Color;
import com.ecs.measure.GUI.Graphics;
import com.ecs.measure.GUI.Object;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;

public class Slider extends Object {
    public double minimumValue, maximumValue, value;
    public Color primaryColor, secondaryColor, pipeColor, valueColor;
    public boolean showValue = true, roundValue = true;
    public String prefix, postfix;
    public int pipeRadius, trackHeight, valueOffset;
    public Runnable onValueChanged;
    
    public Slider(int x, int y, int width, int trackHeight, int valueOffset, int pipeRadius, double minimumValue, double maximumValue, double value, Color primaryColor, Color secondaryColor, Color pipeColor, Color valueColor, String prefix, String postfix) {
        super(x, y, width, pipeRadius * 2 + valueOffset + 7);
        this.trackHeight = trackHeight;
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
        this.value = value;
        this.valueOffset = valueOffset;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.pipeColor = pipeColor;
        this.valueColor = valueColor;
        this.pipeRadius = pipeRadius;
        this.prefix = prefix;
        this.postfix = postfix;
        
        onMouseEvent = (mouseX, mouseY) -> {
            if (hovered && Mouse.isButtonDown(0)) {
                this.value = (mouseX - screenX) / (double) width * (maximumValue - minimumValue);
                if (mouseX == screenX + width - 1) {
                    this.value = maximumValue;
                }

                if (onValueChanged != null) {
                    onValueChanged.run();
                }
            }
        };
    }
    
    @Override
    public void draw() {
        int pipeLeft = (int) (value / (maximumValue - minimumValue) * width);
        int halfTrackHeight = trackHeight / 2;
        int trackY = screenY + pipeRadius - halfTrackHeight;
        
        Graphics.drawRectangle(screenX, trackY, pipeLeft, trackHeight, primaryColor);
        Graphics.drawRectangle(screenX + pipeLeft, trackY, width - pipeLeft, trackHeight, secondaryColor);
        Graphics.drawCircle(screenX + pipeLeft, trackY + halfTrackHeight, pipeRadius, 16, pipeColor);
        
        if (showValue) {
            String text = prefix + (roundValue ? (int) Math.round(value) : String.format("%.2f:", value)) + postfix;
            Graphics.drawText(
                screenX + width / 2 - Minecraft.getMinecraft().fontRenderer.getStringWidth(text) / 2,
                screenY + pipeRadius * 2 + valueOffset,
                valueColor,
                text,
                false
            );
        }
    }
}