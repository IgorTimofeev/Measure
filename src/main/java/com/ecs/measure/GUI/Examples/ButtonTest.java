package com.ecs.measure.GUI.Examples;

import com.ecs.measure.GUI.Animation;
import com.ecs.measure.GUI.Screen;
import com.ecs.measure.GUI.Objects.Button;
import com.ecs.measure.GUI.Containers.FittedLayout;
import com.ecs.measure.GUI.Objects.FittedPanel;
import com.ecs.measure.Renderers.MeasureRenderer;
import com.ecs.measure.GUI.Color;
import net.minecraft.util.text.TextComponentTranslation;

public class ButtonTest extends Screen {
    private static final Color defaultBackground = new Color(1, 1, 1, 0.12f);
    private static final Color defaultText = new Color(1, 1, 1, 0.5f);
    private static final Color hoveredBackground = new Color(1, 1, 1, 0.3f);
    private static final Color hoveredText = new Color(1, 1, 1, 0.5f);
    private static final Color pressedBackground = Color.WHITE;
    private static final Color pressedText = Color.BLACK;
    
    private void addButton(FittedLayout fittedLayout, String translationKey, boolean state, boolean switchMode, Runnable runnable) {
        Button button = new Button(0, 0, 140, 19,
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
    
    public ButtonTest() {
        FittedPanel fittedPanel = new FittedPanel(0, 0, container.width, container.height, new Color(0, 0, 0, 0.6f));
        fittedPanel.animation = new Animation(150, position -> {
            fittedPanel.color = Color.transition(Color.TRANSPARENT, new Color(0, 0, 0, 0.6f), position);
        });
        container.addChild(fittedPanel);
        
//        Button button = new Button(20, 20, 300, 20, defaultBackground, defaultText, hoveredBackground, hoveredText, pressedBackground, pressedText, "Meow");
//        container.addChild(button);
        
        FittedLayout fittedLayout = new FittedLayout(0, 0, container.width, container.height);
        container.addChild(fittedLayout);

        addButton(fittedLayout, "clearPins", false, false, () -> {
            MeasureRenderer.pins.clear();
        });

        addButton(fittedLayout, "snapToBlocks", MeasureRenderer.snapToBlocks, true, () -> {
            MeasureRenderer.snapToBlocks = !MeasureRenderer.snapToBlocks;
        });

        addButton(fittedLayout, "renderDistance", MeasureRenderer.renderDistance, true, () -> {
            MeasureRenderer.renderDistance = !MeasureRenderer.renderDistance;
        });

        addButton(fittedLayout, "moveDistance", MeasureRenderer.moveDistance, true, () -> {
            MeasureRenderer.moveDistance = !MeasureRenderer.moveDistance;
        });

        addButton(fittedLayout, "renderBlocks", MeasureRenderer.renderBlocks, true, () -> {
            MeasureRenderer.renderBlocks = !MeasureRenderer.renderBlocks;
        });

        addButton(fittedLayout, "renderPolygon", MeasureRenderer.renderPolygon, true, () -> {
            MeasureRenderer.renderPolygon = !MeasureRenderer.renderPolygon;
        });

//        addButton(fittedLayout, "autoPin", MeasureRenderer.autoPinTimerTask != null, true, () -> {
//            MeasureRenderer.toggleAutoPin();
//        });

//        addButton(fittedLayout, "wallhackToggle", WallHackRenderer.wallhackEnabled, true, () -> {
//            WallHackRenderer.wallhackEnabled = !WallHackRenderer.wallhackEnabled;
//        });
    }
}