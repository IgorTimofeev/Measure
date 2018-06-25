package com.ecs.measure.Utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

public class Orientation {
    public static Vec3d applyPartialTicks(EntityLivingBase entity, double partialTicks) {
        return new Vec3d(
            entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks,
            entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks,
            entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks
        );
    }

    public static Vec3d applyPartialTicks(Entity entity, double partialTicks) {
        return new Vec3d(
            entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks,
            entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks,
            entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks
        );
    }
    
    public static Vec3d getMiddlePoint(Vec3d v1, Vec3d v2) {
        return new Vec3d(
            v1.x + (v2.x - v1.x) / 2.0d,
            v1.y + (v2.y - v1.y) / 2.0d,
            v1.z + (v2.z - v1.z) / 2.0d
        );
    }
    
    public static double normalizeAngle(double angle) {
        return (angle %= 360) >= 0 ? angle : (angle + 360);
    }
}