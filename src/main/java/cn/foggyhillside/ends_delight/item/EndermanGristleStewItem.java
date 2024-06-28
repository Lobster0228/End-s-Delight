package cn.foggyhillside.ends_delight.item;

import cn.foggyhillside.ends_delight.EndermanGristleTransport;
import cn.foggyhillside.ends_delight.config.EDCommonConfigs;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import vectorwing.farmersdelight.common.item.ConsumableItem;

public class EndermanGristleStewItem extends ConsumableItem {

    private final Float damage;

    private final Boolean shift;

    public EndermanGristleStewItem(Settings properties, float damage, boolean shift, boolean hasFoodEffectTooltip) {
        super(properties, hasFoodEffectTooltip);
        this.damage = damage;
        this.shift = shift;
    }


    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        ItemStack itemstack = super.finishUsing(stack, world, user);
        if (EDCommonConfigs.GRISTLE_TELEPORT.get() && (!shift || user.isSneaking())) {
            if (!world.isClient()) {
                double d = user.getX();
                double e = user.getY();
                double f = user.getZ();

                for (int i = 0; i < 16; ++i) {
                    double g = user.getX() + (user.getRandom().nextDouble() - 0.5D) * 8.0D;
                    double h = MathHelper.clamp(user.getY() + (double) (user.getRandom().nextInt(EDCommonConfigs.TELEPORT_RANGE_SIZE.get()) + EDCommonConfigs.TELEPORT_MAX_HEIGHT.get() + 1 - EDCommonConfigs.TELEPORT_RANGE_SIZE.get()), (double) world.getBottomY(), (double) (world.getBottomY() + ((ServerWorld) world).getLogicalHeight() - 1));
                    double j = user.getZ() + (user.getRandom().nextDouble() - 0.5D) * 8.0D;
                    if (user.hasVehicle()) {
                        user.stopRiding();
                    }

                    Vec3d vec3d = user.getPos();
                    if (EndermanGristleTransport.randomTeleport(user, g, h, j, true, damage)) {
                        world.emitGameEvent(GameEvent.TELEPORT, vec3d, GameEvent.Emitter.of(user));
                        SoundEvent soundEvent = user instanceof FoxEntity ? SoundEvents.ENTITY_FOX_TELEPORT : SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT;
                        world.playSound((PlayerEntity) null, d, e, f, soundEvent, SoundCategory.PLAYERS, 1.0F, 1.0F);
                        user.playSound(soundEvent, 1.0F, 1.0F);
                        break;
                    }
                }

                if (user instanceof PlayerEntity) {
                    ((PlayerEntity) user).getItemCooldownManager().set(this, 20);
                }
            }
        }
        return itemstack;
    }

}
