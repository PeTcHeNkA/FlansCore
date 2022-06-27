package ru.mrpetchenka.flanscore.common.entity.gun;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

public class EntityTracerGloomy {

    private static final float degToRad = 0.017453292F;
    private static final float BORDER_EXPAND_LENGTH = 0.2F;

    public static EntityTracerGloomy.BulletHitPosition trace(World w, Vec3 fromVec, float yaw, float pitch, float spread, float maxLength, Entity entityToExclude) {
        Vec3 lookVec = createLookVec(yaw, pitch, spread, maxLength);
        Vec3 toVec = Vec3.createVectorHelper(fromVec.xCoord + lookVec.xCoord, fromVec.yCoord + lookVec.yCoord, fromVec.zCoord + lookVec.zCoord);
        return trace(w, fromVec, toVec, entityToExclude);
    }

    public static EntityTracerGloomy.BulletHitPosition trace(World w, Vec3 fromVec, Vec3 toVec, Entity entityToExclude) {
        Vec3 fromVecCopy = Vec3.createVectorHelper(fromVec.xCoord, fromVec.yCoord, fromVec.zCoord);
        Vec3 toVecCopy = Vec3.createVectorHelper(toVec.xCoord, toVec.yCoord, toVec.zCoord);
        MovingObjectPosition collidingBlockMop = w.func_147447_a(fromVecCopy, toVecCopy, false, false, true);
        EntityTracerGloomy.BulletHitPosition collidingBlock = null;

        if (collidingBlockMop != null)
            collidingBlock = new EntityTracerGloomy.BulletHitPosition(collidingBlockMop.blockX, collidingBlockMop.blockY, collidingBlockMop.blockZ, collidingBlockMop.sideHit, collidingBlockMop.hitVec);
        else
            collidingBlock = new EntityTracerGloomy.BulletHitPosition((int) toVec.xCoord, (int) toVec.yCoord, (int) toVec.zCoord, -1, toVec);

        Vec3 newToVec = collidingBlock.hitVec;
        HashMap<Entity, Vec3> collidingEntities = traceEntitiesSimple(w, fromVec, newToVec, entityToExclude);
        EntityTracerGloomy.BulletHitPosition hitPosition;

        if (collidingEntities.size() != 0)
            hitPosition = getClosestEntity(collidingEntities, fromVec, toVec, collidingBlock);
        else hitPosition = collidingBlock;

        return hitPosition;
    }

    private static HashMap<Entity, Vec3> traceEntitiesSimple(World w, Vec3 fromVec, Vec3 toVec, Entity entityToExclude) {
        AxisAlignedBB scannedAABB = AxisAlignedBB.getBoundingBox(Math.min(fromVec.xCoord, toVec.xCoord), Math.min(fromVec.yCoord, toVec.yCoord), Math.min(fromVec.zCoord, toVec.zCoord), Math.max(fromVec.xCoord, toVec.xCoord), Math.max(fromVec.yCoord, toVec.yCoord), Math.max(fromVec.zCoord, toVec.zCoord));
        List<Entity> tracedEntities = w.getEntitiesWithinAABBExcludingEntity(entityToExclude, scannedAABB);
        HashMap<Entity, Vec3> collidingEntities = new HashMap();

        for (Entity entity : tracedEntities) {
            if (entity.canBeCollidedWith() && (entityToExclude == null || entity != entityToExclude.ridingEntity)) {
                float borderSize = entity.getCollisionBorderSize() + 0.2F;
                AxisAlignedBB entityAABB = entity.boundingBox.expand((double) borderSize, (double) borderSize, (double) borderSize);

                if (entityAABB.isVecInside(fromVec)) {
                    collidingEntities.put(entity, fromVec);
                } else {
                    MovingObjectPosition mop = entityAABB.calculateIntercept(fromVec, toVec);

                    if (mop != null) collidingEntities.put(entity, mop.hitVec);
                }
            }
        }

        return collidingEntities;
    }

    private static Vec3 createLookVec(float yaw, float pitch, float spread, float length) {
        float cosYaw = MathHelper.cos(-yaw * 0.017453292F - 3.1415927F);
        float sinYaw = MathHelper.sin(-yaw * 0.017453292F - 3.1415927F);
        float cosPitch = -MathHelper.cos(-pitch * 0.017453292F);
        float sinPitch = MathHelper.sin(-pitch * 0.017453292F);
        Vec3 lookVec = Vec3.createVectorHelper((double) (sinYaw * cosPitch * length), (double) (sinPitch * length), (double) (cosYaw * cosPitch * length));

        if (spread > 0.0F) addSpreadToVec3(lookVec, spread);

        return lookVec;
    }

    private static void addSpreadToVec3(Vec3 vec, float spread) {
        Random rand = new Random();
        float randAngle = (rand.nextFloat() - 0.5F) * spread * 2.0F * 0.017453292F;
        Vec3 axis = Vec3.createVectorHelper((double) (rand.nextFloat() - 0.5F), (double) (rand.nextFloat() - 0.5F), (double) (rand.nextFloat() - 0.5F));
        axis.normalize();
        float s = MathHelper.sin(randAngle);
        float c = MathHelper.cos(randAngle);
        float oc = 1.0F - c;
        double[] m = new double[]{(double) oc * axis.xCoord * axis.xCoord + (double) c, (double) oc * axis.xCoord * axis.yCoord - axis.zCoord * (double) s, (double) oc * axis.zCoord * axis.xCoord + axis.yCoord * (double) s, (double) oc * axis.xCoord * axis.yCoord + axis.zCoord * (double) s, (double) oc * axis.yCoord * axis.yCoord + (double) c, (double) oc * axis.yCoord * axis.zCoord - axis.xCoord * (double) s, (double) oc * axis.zCoord * axis.xCoord - axis.yCoord * (double) s, (double) oc * axis.yCoord * axis.zCoord + axis.xCoord * (double) s, (double) oc * axis.zCoord * axis.zCoord + (double) c};
        vec.xCoord = m[0] * vec.xCoord + m[1] * vec.yCoord + m[2] * vec.zCoord;
        vec.yCoord = m[3] * vec.xCoord + m[4] * vec.yCoord + m[5] * vec.zCoord;
        vec.zCoord = m[6] * vec.xCoord + m[7] * vec.yCoord + m[8] * vec.zCoord;
    }

    private static EntityTracerGloomy.BulletHitPosition getClosestEntity(HashMap<Entity, Vec3> entityToHitVecMap, Vec3 fromVec, Vec3 toVec, EntityTracerGloomy.BulletHitPosition collidingBlock) {
        Entity closestEntity = null;
        Vec3 closestHitVec = null;
        double distanceSq = Double.MAX_VALUE;

        for (Entry<Entity, Vec3> entry : entityToHitVecMap.entrySet()) {
            double entryDistanceSq = fromVec.squareDistanceTo((Vec3) entry.getValue());

            if (entryDistanceSq < distanceSq) {
                closestEntity = (Entity) entry.getKey();
                closestHitVec = (Vec3) entry.getValue();
                distanceSq = entryDistanceSq;
            }
        }

        if (closestEntity != null) return new EntityTracerGloomy.BulletHitPosition(closestEntity, closestHitVec);
        else return collidingBlock;
    }

    public static class BulletHitPosition extends MovingObjectPosition {

        public float distance;

        public BulletHitPosition(int x, int y, int z, int sideHit, Vec3 hitVec) {
            super(x, y, z, sideHit, hitVec);
        }

        public BulletHitPosition(Entity entity, Vec3 hitVec) {
            super(entity);
            super.hitVec = hitVec;
        }

        public String toString() {
            if (super.typeOfHit == MovingObjectType.ENTITY) return "EntityHit";

            return "TileHit: x=" + super.blockX + ", y=" + super.blockY + ", z=" + super.blockZ + ", side=" + super.sideHit;
        }
    }
}
