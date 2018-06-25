package com.ecs.measure.GUI.Objects;

import com.ecs.measure.GUI.Animation;
import com.ecs.measure.GUI.Color;
import com.ecs.measure.GUI.Graphics;
import com.ecs.measure.GUI.Object;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;

public class Button extends Object {
    public Color backgroundDefaultColor, textDefaultColor, backgroundHoveredColor, textHoveredColor, backgroundPressedColor, textPressedColor;
    public String text;
    public Runnable onMousePressed;
    public long animationDuration = 100;

    private boolean switchMode = false, state = false;
    private Color backgroundCurrentColor, textCurrentColor;
    
    public Button(int x, int y, int width, int height, Color backgroundDefaultColor, Color textDefaultColor, Color backgroundHoveredColor, Color textHoveredColor, Color backgroundPressedColor, Color textPressedColor, String text) {
        super(x, y, width, height);

        this.backgroundDefaultColor = backgroundDefaultColor;
        this.textDefaultColor = textDefaultColor;
        this.backgroundHoveredColor = backgroundHoveredColor;
        this.textHoveredColor = textHoveredColor;
        this.backgroundPressedColor = backgroundPressedColor;
        this.textPressedColor = textPressedColor;
        this.text = text;
        
        onMouseEvent = (mouseX, mouseY) -> {
            if (hovered && Mouse.getEventButtonState() && Mouse.getEventButton() == 0) {
                if (switchMode)
                    this.state = !this.state;
                else
                    this.state = true;
                
                if (onMousePressed != null)
                    onMousePressed.run();
            }
            else {
                if (!switchMode)
                    this.state = false;
            }

            if (state)
                setAnimation(backgroundPressedColor, textPressedColor);    
            else if (hovered)
                setAnimation(backgroundHoveredColor, textHoveredColor);    
            else
                setAnimation(backgroundDefaultColor, textDefaultColor);    
        };

        setState(false);
    }

    public void setSwitchMode(boolean value) {
        this.switchMode = value;
    }

    public boolean getSwitchMode() {
        return switchMode;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;

        if (this.state) {
            backgroundCurrentColor = backgroundPressedColor;
            textCurrentColor = textPressedColor;
        }
        else if (hovered) {
            backgroundCurrentColor = backgroundHoveredColor;
            textCurrentColor = textHoveredColor;
        }
        else {
            backgroundCurrentColor = backgroundDefaultColor;
            textCurrentColor = textDefaultColor;
        }
    }

    private void setAnimation(Color backgroundColor, Color textColor) {
        animation = new Animation(animationDuration, (position) -> {
            backgroundCurrentColor = Color.transition(backgroundCurrentColor, backgroundColor, position);
            textCurrentColor = Color.transition(textCurrentColor, textColor, position);
        });
    }
    
    public Button setOnMousePressed(Runnable runnable) {
        onMousePressed = runnable;
        return this;
    }
    
    @Override
    public void draw() {
        Graphics.drawRectangle(screenX, screenY, width, height, backgroundCurrentColor);
        Graphics.drawText(screenX + width / 2 - Minecraft.getMinecraft().fontRenderer.getStringWidth(text) / 2, screenY + height / 2 - 3, textCurrentColor, text, false);
    }
}
