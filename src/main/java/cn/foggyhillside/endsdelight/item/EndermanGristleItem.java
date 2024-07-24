package cn.foggyhillside.endsdelight.item;

import cn.foggyhillside.endsdelight.EndermanGristleTransport;
import cn.foggyhillside.endsdelight.config.EDCommonConfigs;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EndermanGristleItem extends Item {

    private final Float damage;

    private final Boolean shift;

    public EndermanGristleItem(Properties properties, float damage, boolean shift) {
        super(properties);
        this.damage = damage;
        this.shift = shift;
    }


    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
        ItemStack itemstack = super.finishUsingItem(itemStack, level, entity);
        if (EDCommonConfigs.GRISTLE_TELEPORT.get() && (!shift || entity.isShiftKeyDown())) {
            if (!level.isClientSide) {
                double d0 = entity.getX();
                double d1 = entity.getY();
                double d2 = entity.getZ();

                for (int i = 0; i < 16; ++i) {
                    double d3 = entity.getX() + (entity.getRandom().nextDouble() - 0.5D) * 8.0D;
                    double d4 = Mth.clamp(entity.getY() + (double) (entity.getRandom().nextInt(EDCommonConfigs.TELEPORT_RANGE_SIZE.get()) + EDCommonConfigs.TELEPORT_MAX_HEIGHT.get() + 1 - EDCommonConfigs.TELEPORT_RANGE_SIZE.get()), (double) level.getMinBuildHeight(), (double) (level.getMinBuildHeight() + ((ServerLevel) level).getLogicalHeight() - 1));
                    double d5 = entity.getZ() + (entity.getRandom().nextDouble() - 0.5D) * 8.0D;
                    if (entity.isPassenger()) {
                        entity.stopRiding();
                    }

                    net.minecraftforge.event.entity.EntityTeleportEvent.ChorusFruit event = net.minecraftforge.event.ForgeEventFactory.onChorusFruitTeleport(entity, d3, d4, d5);
                    if (event.isCanceled()) return itemstack;
                    if (EndermanGristleTransport.randomTeleport(entity, d3, d4, d5, true, damage)) {
                        SoundEvent soundevent = entity instanceof Fox ? SoundEvents.FOX_TELEPORT : SoundEvents.CHORUS_FRUIT_TELEPORT;
                        level.playSound((Player) null, d0, d1, d2, soundevent, SoundSource.PLAYERS, 1.0F, 1.0F);
                        entity.playSound(soundevent, 1.0F, 1.0F);
                        break;
                    }
                }

                if (entity instanceof Player) {
                    ((Player) entity).getCooldowns().addCooldown(this, 20);
                }
            }
        }
        return itemstack;
    }

}
