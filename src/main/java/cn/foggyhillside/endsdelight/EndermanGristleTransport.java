package cn.foggyhillside.endsdelight;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class EndermanGristleTransport {

    public static final DamageSource GRISTLE_TELEPORT = (new DamageSource("ends_delight.enderman_gristle_teleport")).setScalesWithDifficulty();

    public static boolean randomTeleport(LivingEntity entity, double x, double y, double z, boolean p_20988_, float damage) {
        double d0 = entity.getX();
        double d1 = entity.getY();
        double d2 = entity.getZ();
        double d3 = y;
        boolean flag = false;
        BlockPos blockpos = new BlockPos(x, y, z);
        Level level = entity.level;
        if (level.hasChunkAt(blockpos)) {
            boolean flag1 = false;

            while (!flag1 && blockpos.getY() > (d1 < level.getMinBuildHeight() ? level.getMinBuildHeight() : d1)) {
                BlockPos blockpos1 = blockpos.below();
                BlockState blockstate1 = level.getBlockState(blockpos1);
                if (blockstate1.getMaterial().blocksMotion() && d3 < (double) (level.getMinBuildHeight() + ((ServerLevel) level).getLogicalHeight() - 2)) {
                    flag1 = true;
                } else {
                    --d3;
                    blockpos = blockpos1;
                }
            }

            if (flag1) {
                entity.teleportTo(x, d3, z);
                if (level.noCollision(entity) && !level.containsAnyLiquid(entity.getBoundingBox())) {
                    flag = true;
                }
                if (flag) {
                    if (entity instanceof Player && !((Player) entity).isCreative()) {
                        if (entity.getHealth() < (entity.getMaxHealth() * 0.3F)) {
                            entity.hurt(GRISTLE_TELEPORT, entity.getHealth() * 1.5F);
                        } else {
                            entity.hurt(GRISTLE_TELEPORT, entity.getHealth() * damage);
                        }
                    }
                }
            }
        }

        if (!flag) {
            entity.teleportTo(d0, d1, d2);
            return false;
        } else {
            if (p_20988_) {
                level.broadcastEntityEvent(entity, (byte) 46);
            }

            if (entity instanceof PathfinderMob) {
                ((PathfinderMob) entity).getNavigation().stop();
            }

            return true;
        }
    }

}
