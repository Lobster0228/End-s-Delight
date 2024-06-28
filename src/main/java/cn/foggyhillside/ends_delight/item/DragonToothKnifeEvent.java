package cn.foggyhillside.ends_delight.item;

import cn.foggyhillside.ends_delight.config.EDCommonConfigs;
import cn.foggyhillside.ends_delight.registry.ModItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class DragonToothKnifeEvent {

    public static class KnifeEvents {
        public KnifeEvents() {
        }

        public static float onAttackEndMobs(LivingEntity livingEntity, DamageSource source, float amount) {
            String[] endMobs = EDCommonConfigs.END_MOBS.get().toArray(new String[0]);
            for (String endMob : endMobs) {
                String[] split = livingEntity.getType().toString().split("\\.");
                if (split[1] != null && split[2] != null) {
                    String id = String.join(":", split[1], split[2]);
                    if (id.equals(endMob)) {
                        LivingEntity attacker = (LivingEntity) source.getAttacker();
                        ItemStack toolStack = attacker != null ? attacker.getStackInHand(Hand.MAIN_HAND) : ItemStack.EMPTY;
                        if (toolStack.getItem().equals(ModItem.DragonToothKnife.get())) {
                            amount *= 3.5F;
                            break;
                        }
                    }
                }
            }
            return amount;
        }
    }
}
