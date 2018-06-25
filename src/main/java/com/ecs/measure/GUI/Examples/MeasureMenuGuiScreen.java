package com.ecs.measure.GUI.Examples;

import com.ecs.measure.GUI.Animation;
import com.ecs.measure.GUI.Graphics.Color;
import com.ecs.measure.GUI.Containers.FittedLayout;
import com.ecs.measure.GUI.Graphics.Texture;
import com.ecs.measure.GUI.Objects.*;
import com.ecs.measure.GUI.Screen;
import com.ecs.measure.Renderers.MeasureRenderer;
import net.minecraft.util.text.TextComponentTranslation;
import org.lwjgl.input.Mouse;

import static com.ecs.measure.Renderers.MeasureRenderer.*;

public class MeasureMenuGuiScreen extends Screen {
    private static final Color defaultBackground = new Color(1, 1, 1, 0.12f);
    private static final Color defaultText = new Color(1, 1, 1, 0.5f);
    private static final Color hoveredBackground = new Color(1, 1, 1, 0.3f);
    private static final Color hoveredText = new Color(1, 1, 1, 0.5f);
    private static final Color pressedBackground = new Color(1, 1, 1, 0.9f);
    private static final Color pressedText = new Color(0, 0, 0);
    private static int objectWidth = 140;


    private void addButton(FittedLayout fittedLayout, String translationKey, boolean state, boolean switchMode, Runnable runnable) {
        Button button = new Button(0, 0, objectWidth, 19,
            defaultBackground,
            defaultText,
            hoveredBackground,
            hoveredText,
            pressedBackground,
            pressedText,
            new TextComponentTranslation(translationKey).getFormattedText()
        ).setOnMousePressed(runnable);
        
        button.setSwitchMode(switchMode);
        button.setState(state);
        fittedLayout.addChild(button);
    }

    int prevX = -1, prevY;

    public MeasureMenuGuiScreen() {
        FittedPanel fittedPanel = new FittedPanel(0, 0, container.width, container.height, new Color(0, 0, 0, 0.6f));
        fittedPanel.animation = new Animation(150, position -> {
            fittedPanel.color = Color.transition(new Color(0, 0, 0, 0), new Color(0, 0, 0, 0.6f), position);
        });
        container.addChild(fittedPanel);

//        Button button = new Button(20, 20, 300, 20, defaultBackground, defaultText, hoveredBackground, hoveredText, pressedBackground, pressedText, "Meow");
//        container.addChild(button);
        
        FittedLayout fittedLayout = new FittedLayout(0, 0, container.width, container.height);
        container.addChild(fittedLayout);

        addButton(fittedLayout, "clearPins", false, false, () -> {
            MeasureRenderer.clearPins();
        });

        addButton(fittedLayout, "snapToBlocks", snapToBlocks, true, () -> {
            snapToBlocks = !snapToBlocks;
        });

        addButton(fittedLayout, "renderDistance", renderDistance, true, () -> {
            renderDistance = !renderDistance;
        });

        addButton(fittedLayout, "moveDistance", moveDistance, true, () -> {
            moveDistance = !moveDistance;
        });

        addButton(fittedLayout, "renderBlocks", renderBlocks, true, () -> {
            renderBlocks = !renderBlocks;
        });

        addButton(fittedLayout, "renderPolygon", renderPolygon, true, () -> {
            renderPolygon = !renderPolygon;
        });

        Slider slider = new Slider(1, 1, objectWidth, 2, 4, 3, 0, 100, 30, new Color(0xFF0000), new Color(0x0), new Color(0xFFFFFF), new Color(0xFFFFFF), "Gleb pidor na ", "%");
        fittedLayout.addChild(slider);
        slider.onValueChanged = () -> {
            slider.valueColor.r = (float) slider.value / (float) (slider.maximumValue - slider.minimumValue);
            slider.valueColor.g = slider.valueColor.r;
            slider.valueColor.b = slider.valueColor.r;
        };
        slider.onValueChanged.run();
        
        Image image = new Image(10, 10, 120, 190, new Texture("/Users/igor/Desktop/2.png"));
        container.addChild(image);
        image.onMouseEvent = (mouseX, mouseY) -> {
            if (image.hovered) {
                if (Mouse.isButtonDown(0)) {
                    int x = (int) ((mouseX - image.screenX) / (float) image.width * image.texture.width);
                    int y = (int) ((mouseY - image.screenY) / (float) image.height * image.texture.height);
                    int radius = 5;
                    
                    int index;
                    Color color;
                    for (int j = y - radius; j <= y + radius; j++) {
                        for (int i = x - radius; i <= x + radius; i++) {
                            index = image.texture.getIndex(i, j);
                            color = image.texture.getPixel(index);
                            image.texture.setPixel(index, color.r, color.g, color.b, 0);
                        } 
                    }
                }
                else if (Mouse.isButtonDown(1)) {
                    if (prevX > -1) {
                        image.x += mouseX - prevX;
                        image.y += mouseY - prevY;
                    }

                    prevX = mouseX;
                    prevY = mouseY;
                }
                else {
                    prevX = -1;
                }
            }
        };
    }
}