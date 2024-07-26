package cn.foggyhillside.ends_delight;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EndermanGristleTransport {

    public static final DamageSource GRISTLE_TELEPORT = (new DamageSource("ends_delight.enderman_gristle_teleport")).setScaledWithDifficulty();

    public static boolean randomTeleport(LivingEntity entity, double x, double y, double z, boolean p_20988_, float damage) {
        double d0 = entity.getX();
        double d1 = entity.getY();
        double d2 = entity.getZ();
        double d3 = y;
        boolean flag = false;
        BlockPos blockpos = new BlockPos(x, y, z);
        World world = entity.getWorld();
        if (world.isChunkLoaded(blockpos)) {
            boolean flag1 = false;

            while (!flag1 && blockpos.getY() > (d1 < world.getBottomY() ? world.getBottomY() : d1)) {
                BlockPos blockpos1 = blockpos.down();
                BlockState blockstate1 = world.getBlockState(blockpos1);
                if (blockstate1.getMaterial().blocksMovement() && d3 < (double) (world.getBottomY() + ((ServerWorld) world).getLogicalHeight() - 2)) {
                    flag1 = true;
                } else {
                    --d3;
                    blockpos = blockpos1;
                }
            }

            if (flag1) {
                entity.requestTeleport(x, d3, z);
                if (world.isSpaceEmpty(entity) && !world.containsFluid(entity.getBoundingBox())) {
                    flag = true;
                }
                if (flag) {
                    if (entity instanceof PlayerEntity && !((PlayerEntity) entity).isCreative()) {
                        if (entity.getHealth() < (entity.getMaxHealth() * 0.3F)) {
                            entity.damage(GRISTLE_TELEPORT, entity.getHealth() * 1.5F);
                        } else {
                            entity.damage(GRISTLE_TELEPORT, entity.getHealth() * damage);
                        }
                    }
                }
            }
        }

        if (!flag) {
            entity.requestTeleport(d0, d1, d2);
            return false;
        } else {
            if (p_20988_) {
                world.sendEntityStatus(entity, (byte) 46);
            }

            if (entity instanceof PathAwareEntity) {
                ((PathAwareEntity) entity).getNavigation().stop();
            }

            return true;
        }
    }

}
