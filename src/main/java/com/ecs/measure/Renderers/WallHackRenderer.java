package com.ecs.measure.Renderers;

import com.ecs.measure.Measure;
import com.ecs.measure.GUI.Color;
import com.ecs.measure.Utils.Orientation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class WallHackRenderer {
    private static final float LOOK_VECTOR_WIDTH = 0.8f;
    private static final float LOOK_VECTOR_LENGTH = 1.3f;
    private static final float REGULAR_LINE_WIDTH = 0.8f;
    private static final float HELPER_LINE_WIDTH = 0.5f;
    private static final double HELPER_LINE_STEP = 0.25d;
    private static final Color LOOK_VECTOR_COLOR = new Color(0xFFFFFFFF);

    private static final ColorScheme NETURAL_COLOR_SCHEME = new ColorScheme(new Color(0xFF88FF88), new Color(0x8088FF88), new Color(0x2088FF88));
    private static final ColorScheme PLAYER_COLOR_SCHEME = new ColorScheme(new Color(0xFF8888FF), new Color(0x808888FF), new Color(0x208888FF));
    private static final ColorScheme AGRESSIVE_COLOR_SCHEME = new ColorScheme(new Color(0xFFFF8888), new Color(0x80FF8888), new Color(0x20FF8888));
    private static final ColorScheme OTHER_COLOR_SCHEME = new ColorScheme(new Color(0xFFFFFFFF), new Color(0x80FFFFFF), new Color(0x20FFFFFF));
    private static final int TEXT_COLOR = 0xFFFFFF;

    private static final String LOCALIZED_HP = new TextComponentTranslation("hp").getFormattedText();
    private static final String LOCALIZED_DISTANCE = new TextComponentTranslation("distance").getFormattedText();
    
    public static boolean wallhackEnabled = false;
    public static boolean wallhackClosestPathEnabled = true;

    private static int maxClosestEntityLinesCount = 5, closestEntityLinesCount;
    private static double textStep = 0.4d, textY;

    private static int closestIndex;
    private static double closestDistance;
    private static Entity closestEntity;
    private static Vec3d closestPosition, closestPrevPosition;
    
    private static class ColorScheme {
        Color regularLine, helperLine, background;
        ColorScheme(Color regularLine, Color helperLine, Color background) {
            this.regularLine = regularLine;
            this.helperLine = helperLine;
            this.background = background;
        }
    }
    
    private void drawText(int color, String text) {
        WorldRenderer.renderCenteredText(0, textY, 0, Measure.minecraft.player.rotationYaw, 0.03d, true, color, text);
        textY += textStep;
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void renderWorldLastEvent(RenderWorldLastEvent event) {
        if (wallhackEnabled) {
            float partialTicks = event.getPartialTicks();
            closestEntityLinesCount = 0;
            World world = Measure.minecraft.player.getEntityWorld();
            Vec3d playerPosition = Orientation.applyPartialTicks(Measure.minecraft.player, partialTicks);

            glEnable(GL_BLEND);
            glDisable(GL_CULL_FACE);
            glDisable(GL_DEPTH_TEST);
            glEnable(GL_LINE_SMOOTH);

            glPushMatrix();
            glTranslated(-playerPosition.x, -playerPosition.y, -playerPosition.z);

            ArrayList<Entity> entities = new ArrayList<>(world.getLoadedEntityList());
            
            closestPrevPosition = Orientation.applyPartialTicks(Measure.minecraft.player, partialTicks);

            while (entities.size() > 0) {
                closestIndex = 0;
                closestDistance = Double.MAX_VALUE;
                
                for (int i = 0; i < entities.size(); i++) {
                    Entity entity = entities.get(i);
                    Vec3d entityPosition = Orientation.applyPartialTicks(entity, partialTicks);
                    
                    double distance = closestPrevPosition.distanceTo(entityPosition);
                    if (distance < closestDistance) {
                        closestIndex = i;
                        closestDistance = distance;
                        closestEntity = entity;
                        closestPosition = entityPosition;
                    }
                }
                
                glDisable(GL_TEXTURE_2D);
                glDisable(GL_DEPTH_TEST);
                
                if (!(closestEntity instanceof EntityPlayer && closestEntity.getName().equals(Measure.minecraft.player.getName()))) {
                    ColorScheme currentColorScheme = OTHER_COLOR_SCHEME;
                    if (closestEntity instanceof EntityMob || closestEntity instanceof EntitySlime) {
                        currentColorScheme = AGRESSIVE_COLOR_SCHEME;
                    } else if (closestEntity instanceof EntityCreature) {
                        currentColorScheme = NETURAL_COLOR_SCHEME;
                    } else if (closestEntity instanceof EntityPlayer) {
                        currentColorScheme = PLAYER_COLOR_SCHEME;
                    }

                    // Рендерим кубик вокруг энтити
                    glPushMatrix();
                    glTranslated(closestPosition.x, closestPosition.y, closestPosition.z);
                    glRotated(-closestEntity.rotationYaw, 0, 1, 0);
                   
                    double halfEntityWidth = closestEntity.width / 2;
                    
                    WorldRenderer.renderBox(
                        -halfEntityWidth,
                        0,
                        -halfEntityWidth,
                        halfEntityWidth,
                        closestEntity.height,
                        halfEntityWidth,
                        currentColorScheme.background
                    );

                    WorldRenderer.renderBoxRegularLines(
                        -halfEntityWidth,
                        0,
                        -halfEntityWidth,
                        halfEntityWidth,
                        closestEntity.height,
                        halfEntityWidth,
                        REGULAR_LINE_WIDTH,
                        currentColorScheme.regularLine
                    );

                    glPopMatrix();

                    // Рендерим всякие тексты, взгляды, шмот и прочее после кубика
                    glEnable(GL_TEXTURE_2D);
                    glPushMatrix();
                    glTranslated(closestPosition.x, closestPosition.y, closestPosition.z);
                    textY = closestEntity.height + 0.6d;

                    // Дистанция до залупы
                    drawText(TEXT_COLOR, LOCALIZED_DISTANCE + (int) playerPosition.distanceTo(closestPosition));

                    if (closestEntity instanceof EntityLiving) {
                        EntityLiving entityLiving = (EntityLiving) closestEntity;

                        // Здоровьечко хуйнины
                        float HP = entityLiving.getHealth() / entityLiving.getMaxHealth();
                        drawText(Color.toARGB(new Color(1.0f - HP, HP, 0.0f, 1.0f)), LOCALIZED_HP + (int) (HP * 100) + "%");

                        // Имя живого говна
                        drawText(TEXT_COLOR, entityLiving.getName());

                        // Рендерим вектор взгляда хуйни
                        glDisable(GL_TEXTURE_2D);
                        glPushMatrix();
                            glRotated(-entityLiving.rotationYawHead - 90, 0, 1, 0);
                            glLineWidth(LOOK_VECTOR_WIDTH);
                            glBegin(GL_LINES);
                                glColor4d(LOOK_VECTOR_COLOR.r, LOOK_VECTOR_COLOR.g, LOOK_VECTOR_COLOR.b, LOOK_VECTOR_COLOR.a);
                                glVertex3d(0, entityLiving.height / 2, 0);
                                glVertex3d(LOOK_VECTOR_LENGTH, entityLiving.height / 2, 0);
                            glEnd();
                        glPopMatrix();
                        glEnable(GL_TEXTURE_2D);
                    }
                    // Имя брошенных айтемов
                    else if (closestEntity instanceof EntityItem) {
                        ItemStack itemStack = ((EntityItem) closestEntity).getItem();

                        // Рендерим линию до этой параши
                        if (wallhackClosestPathEnabled && closestEntityLinesCount <= maxClosestEntityLinesCount) {
                            glDisable(GL_TEXTURE_2D);
                            glPushMatrix();
                                glBegin(GL_LINES);
                                    glColor4f(1, 1, 1, 0.5f);
                                    glVertex3d(closestPrevPosition.x - closestPosition.x, closestPrevPosition.y - closestPosition.y, closestPrevPosition.z - closestPosition.z);
                                    glVertex3d(0, 0, 0);
                                glEnd();
                            glPopMatrix();
                            glEnable(GL_TEXTURE_2D);

                            closestPrevPosition = closestPosition;
                            closestEntityLinesCount++;
                        }

                        drawText(TEXT_COLOR, itemStack.getDisplayName() + ", " + itemStack.getCount());
                    }
                    // Имя всего остального
                    else {
                        drawText(TEXT_COLOR, closestEntity.getName());
                    }
                    
                    glPopMatrix();
                }
                
                entities.remove(closestIndex);
            }
            
            glPopMatrix();

            glEnable(GL_TEXTURE_2D);
            glDisable(GL_BLEND);
            glEnable(GL_CULL_FACE);
            glEnable(GL_DEPTH_TEST);
        }
    }
}
