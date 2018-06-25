package com.ecs.measure.Renderers;

import com.ecs.measure.Measure;
import com.ecs.measure.Handlers.SoundHandler;
import com.ecs.measure.GUI.Color;
import com.ecs.measure.Utils.Orientation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static org.lwjgl.opengl.GL11.*;

public class MeasureRenderer {
    private static final double PIN_SEGMENTS = 24.0d;
    private static final double PIN_ANGLE = Math.PI * 2 / PIN_SEGMENTS;
    private static final double PIN_COS = Math.cos(PIN_ANGLE), PIN_SIN = Math.sin(PIN_ANGLE);
    private static final double PIN_LENGTH_RADIUS = 0.06d;
    private static final double PIN_LENTH_OFFSET = PIN_LENGTH_RADIUS * 5;
    private static final Color PING_BLOCK_LINE_COLOR = new Color(1, 1, 1, 0.4f);
    private static final Color PIN_POLYGON_COLOR = new Color(1, 1, 1, 0.12f);
    private static final float PIN_LIFETIME_LIMIT = 5;
    private static final double AUTO_PIN_EVERY = 1.0d;
    private static final int AUTO_PIN_TIMER_INTERVAL = 100;
    private static final double RAY_TRACE_LENGTH = 80.0d;
    
    public static boolean moveDistance = true;
    public static boolean renderDistance = true;
    public static boolean renderBlocks = true;
    public static boolean snapToBlocks = true;
    public static boolean renderPolygon = false;
    public static ArrayList<Pin> pins = new ArrayList<>();
    
    public static TimerTask autoPinTimerTask = null;
    private static float partialTicks = 0;
    private static Timer autoPinTimer = new Timer();
    private static Vec3d autoPinPreviousPosition = null;
    
    public static class Pin {
        Vec3d hit, block;
        float scale = 0.2f;

        Pin(Vec3d hit, Vec3d block) {
            this.hit = hit;
            this.block = block;
        }

        Pin(Vec3d hit) {
            this.hit = hit;
            this.block = null;
        }
    }
    
    public static void clearPins() {
        pins.clear();
        Measure.minecraft.player.playSound(SoundHandler.PIN_CLEARED, 1, 1);
    }
    
    public static void addLookingAt() {
        RayTraceResult rayTraceResult = Measure.minecraft.player.rayTrace(RAY_TRACE_LENGTH, partialTicks);
        if (rayTraceResult != null && rayTraceResult.hitVec.distanceTo(Measure.minecraft.player.getPositionVector()) < RAY_TRACE_LENGTH) {
            BlockPos blockPos = rayTraceResult.getBlockPos();
            Vec3d blockVector = new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());

            boolean exists = false;
            if (snapToBlocks) {
                Pin pin;
                for (int i = 0; i < pins.size(); i++) {
                    pin = pins.get(i);
                    if (pin.block != null && pin.block.equals(blockVector)) {
                        pins.remove(i);
                        exists = true;
                        i--;
                        break;
                    }
                }
            }
            
            if (!exists) {
                pins.add(new Pin(snapToBlocks ? blockVector.addVector(0.5,0.5, 0.5) : rayTraceResult.hitVec, blockVector));
                Measure.minecraft.player.playSound(SoundHandler.PIN_PLACED, 1, 1);
            }
        }
    }
    
    public static void toggleAutoPin() {
        if (autoPinTimerTask == null) {
            autoPinTimerTask = new TimerTask() {
                @Override
                public void run() {
                    Vec3d position = Measure.minecraft.player.getPositionVector();

                    if (autoPinPreviousPosition == null) {
                        autoPinPreviousPosition = position;
                    }
                    else if (autoPinPreviousPosition.distanceTo(position) >= AUTO_PIN_EVERY) {
                        pins.add(new Pin(Measure.minecraft.player.getPositionVector()));
                        autoPinPreviousPosition = position;
                    }
                }
            };

            autoPinTimer.scheduleAtFixedRate(autoPinTimerTask, 0, AUTO_PIN_TIMER_INTERVAL);
        }
        else {
            autoPinTimerTask.cancel();
            autoPinTimerTask = null;
        }
    }

    private void renderCircle(double xCenter, double yCenter, double zCenter, double radius) {
        double x = radius, y = 0, t;

        glBegin(GL_TRIANGLE_FAN);       
            for (int i = 0; i < PIN_SEGMENTS; i++) {
                glVertex3d(xCenter + x, yCenter + y, zCenter + 0);
                
                t = x;
                x = PIN_COS * x - PIN_SIN * y;
                y = PIN_SIN * t + PIN_COS * y;
            }
        glEnd();
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void renderWorldLastEvent(RenderWorldLastEvent event) {
        if (pins.size() > 0) {
            partialTicks = event.getPartialTicks();
            Vec3d playerPosition = Orientation.applyPartialTicks(Measure.minecraft.player, partialTicks);
            Pin previousPin = pins.get(0);
            
            glEnable(GL_BLEND);
            glDisable(GL_CULL_FACE);
            glDisable(GL_DEPTH_TEST);
            glDisable(GL_TEXTURE_2D);
            glEnable(GL_LINE_SMOOTH);
            
            glPushMatrix();
                glTranslated(-playerPosition.x, -playerPosition.y, -playerPosition.z);
                
                if (renderPolygon) {
                    glBegin(GL_POLYGON);
                    glColor4d(PIN_POLYGON_COLOR.r, PIN_POLYGON_COLOR.g, PIN_POLYGON_COLOR.b, PIN_POLYGON_COLOR.a);
                    for (int i = 0; i < pins.size(); i++) {
                        Pin pin = pins.get(i);
                        glVertex3d(pin.hit.x, pin.hit.y, pin.hit.z);
                    }
                    glEnd();
                }
                for (int i = 0; i < pins.size(); i++) {
                    Pin currentPin = pins.get(i);

                    // Кубик текущей
                    if (renderBlocks && currentPin.block != null) {
                        WorldRenderer.renderBoxRegularLines(
                            currentPin.block.x, 
                            currentPin.block.y, 
                            currentPin.block.z, 
                            currentPin.block.x + 1, 
                            currentPin.block.y + 1, 
                            currentPin.block.z + 1,
                            1.2f,
                            PING_BLOCK_LINE_COLOR
                        );
                    }

                    // Масштабирование пина в зависимости от его "жызы"
                    currentPin.scale += partialTicks / PIN_LIFETIME_LIMIT;
                    if (currentPin.scale > 1) currentPin.scale = 1;
                    
                    // Кружочек текущей
                    glColor3d(1, 1,1);
                    glPushMatrix();
                        glTranslated(currentPin.hit.x, currentPin.hit.y, currentPin.hit.z);
                        glRotated(-Measure.minecraft.player.rotationYaw, 0, 1, 0);
                        glRotated(Measure.minecraft.player.rotationPitch, 1, 0, 0);
                        glScalef(currentPin.scale, currentPin.scale, currentPin.scale);
                        
                        renderCircle(0, 0, 0, 0.04d);
                    glPopMatrix();
                    
                    if (i > 0) {
                        // Линия между двумя
                        glLineWidth(1.2f);
                        glColor4f(1, 1,1, 1);
                        glBegin(GL_LINES);
                            glVertex3d(previousPin.hit.x, previousPin.hit.y, previousPin.hit.z);
                            glVertex3d(currentPin.hit.x, currentPin.hit.y, currentPin.hit.z);
                        glEnd();

                        // Рендер дистанционной хуйни этой скругленной
                        if (renderDistance) {
                            double distance;
                            String text;
                            if (renderBlocks && currentPin.block != null && previousPin.block != null) {
                                distance = previousPin.block.distanceTo(currentPin.block);
                                text = Integer.toString((int) (distance + 1));
                            }
                            else {
                                distance = previousPin.hit.distanceTo(currentPin.hit);
                                text = String.format("%.2f", distance);
                            }
                            
                            double width = (text.length() + 2) * 0.045;

                            Vec3d distanceMarker;
                            if (moveDistance) {
                                // Считаем площадь пар-ма, образованного вектором между первой точкой и игроком и вектором между первой и второй точкой, находим его высоту, а затем кусочек от первой точки до точки высоты
                                Vec3d vectorBetweenPoints = currentPin.hit.subtract(previousPin.hit);
                                Vec3d vectorBetweenPlayer = playerPosition.addVector(0, Measure.minecraft.player.eyeHeight, 0).subtract(previousPin.hit);
                                double distanceBetweenPoints = vectorBetweenPoints.lengthVector();
                                double w = Math.sqrt(Math.pow(vectorBetweenPlayer.lengthVector(), 2) - Math.pow(vectorBetweenPoints.crossProduct(vectorBetweenPlayer).lengthVector() / distanceBetweenPoints, 2));
                                double t = w / distanceBetweenPoints;

                                // Чекаем, чтоб залупонька всегда была в своих границах
                                t = vectorBetweenPlayer.dotProduct(vectorBetweenPoints) > 0 ? (t > 1 ? 1 : t) : 0;

                                // Чекаем, чтоб залупонька была чуть ближе к центру от исходных границ
                                double widthPart = PIN_LENTH_OFFSET / distanceBetweenPoints;
                                if (t < widthPart) {
                                    t = widthPart;
                                } else if (t > 1 - widthPart) {
                                    t = 1 - widthPart;
                                }

                                //                            System.out.println("dot = " + dotProduct + ",area = " + area + ", distanceBetweenPoints = " + distanceBetweenPoints + ", distanceBetweenPlayer = " + distanceBetweenPlayer + ", h =" + h + ", w = " + w);

                                distanceMarker = new Vec3d(
                                    previousPin.hit.x + (currentPin.hit.x - previousPin.hit.x) * t,
                                    previousPin.hit.y + (currentPin.hit.y - previousPin.hit.y) * t,
                                    previousPin.hit.z + (currentPin.hit.z - previousPin.hit.z) * t
                                );
                            }
                            else {
                                distanceMarker = Orientation.getMiddlePoint(previousPin.hit, currentPin.hit);
                            }

                            double hatan = Orientation.normalizeAngle(Math.toDegrees(Math.atan2(previousPin.hit.x - currentPin.hit.x, previousPin.hit.z - currentPin.hit.z)));
                            double yaw = Orientation.normalizeAngle(hatan + Measure.minecraft.player.rotationYaw);
                            double deg = Math.toDegrees(Math.atan2(Math.sqrt(Math.pow(currentPin.hit.x - previousPin.hit.x, 2) + Math.pow(currentPin.hit.z - previousPin.hit.z, 2)), currentPin.hit.y - previousPin.hit.y));

                            glColor3d(1, 1, 1);
                            glPushMatrix();
                                glTranslated(distanceMarker.x, distanceMarker.y, distanceMarker.z);
                                glRotated((yaw > 180 ? hatan + 90 : hatan - 90), 0, 1, 0);
                                glRotated((yaw > 180 ? 90 - deg : deg - 90), 0, 0, 1);
                                glRotated(Measure.minecraft.player.rotationPitch, 1, 0, 0);
                                glScalef(currentPin.scale, currentPin.scale, currentPin.scale);
                            
                                glPushMatrix();
                                    glTranslated(-width / 2, 0, 0);
                                    renderCircle(0, 0, 0, PIN_LENGTH_RADIUS);
        
                                    glBegin(GL_QUADS);
                                        glVertex3d(0, PIN_LENGTH_RADIUS, 0);
                                        glVertex3d(width, PIN_LENGTH_RADIUS, 0);
                                        glVertex3d(width, -PIN_LENGTH_RADIUS, 0);
                                        glVertex3d(0, -PIN_LENGTH_RADIUS, 0);
                                    glEnd();
        
                                    renderCircle(width, 0, 0, PIN_LENGTH_RADIUS);
                                glPopMatrix();
                                
                                glEnable(GL_TEXTURE_2D);
                                glColor3d(0, 0, 0);
                                glPushMatrix();
                                    glRotated(180, 1, 0, 0);
                                    glRotated(180, 0, 1, 0);
                                    glScaled(0.011, 0.011, 0.011);
                                    Measure.minecraft.fontRenderer.drawString(text, -Measure.minecraft.fontRenderer.getStringWidth(text) / 2, -3.2f, 0x0, false);
                                glPopMatrix();
                                glDisable(GL_TEXTURE_2D);
                            glPopMatrix();
                        }
                    }
                    
                    previousPin = currentPin;
                }
            glPopMatrix();

            glDisable(GL_BLEND);
            glEnable(GL_CULL_FACE);
            glEnable(GL_DEPTH_TEST);
            glEnable(GL_TEXTURE_2D);
            glDisable(GL_LINE_SMOOTH);
        }
    }
}
