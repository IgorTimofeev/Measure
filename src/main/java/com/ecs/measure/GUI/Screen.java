package com.ecs.measure.GUI;

import com.ecs.measure.Measure;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

public class Screen extends GuiScreen {
    public MainContainer container;
    
    public class MainContainer extends Container {
        MainContainer(int x, int y, int width, int height) {
            super(x, y, width, height);
        }

        @Override
        public void update() {
            ScaledResolution scaledResolution = new ScaledResolution(Measure.minecraft);
            width = scaledResolution.getScaledWidth();
            height = scaledResolution.getScaledHeight();
            
            super.update();
        }
    }
    
    public Screen() {
        this.container = new MainContainer(0, 0, Measure.minecraft.displayWidth, Measure.minecraft.displayHeight);
    }
    
    private void handleMouse(Container container, int mouseX, int mouseY) {
        Object child;
        for (int i = container.children.size() - 1; i >= 0; i--) {
            child = container.children.get(i);
            
            if (child instanceof Container) {
                handleMouse((Container) child, mouseX, mouseY);
            }
            else {
                if (!child.hidden && !child.disabled) {
                    child.hovered = child.isPointInside(mouseX, mouseY);
                    
                    if (child.onMouseEvent != null)
                        child.onMouseEvent.run(mouseX, mouseY);
                }
                else {
                    child.hovered = false;
                }
            }
        }
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void handleMouseInput() {
        int scaleFactor = new ScaledResolution(Measure.minecraft).getScaleFactor();
        handleMouse(
            container,
            Mouse.getEventX() / scaleFactor,
            (Measure.minecraft.displayHeight - Mouse.getEventY() - 3) / scaleFactor
        );
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        container.update();
        container.draw();
    }
}
