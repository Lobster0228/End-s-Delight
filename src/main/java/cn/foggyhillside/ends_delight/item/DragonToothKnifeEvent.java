package cn.foggyhillside.ends_delight.item;

import cn.foggyhillside.ends_delight.config.EDCommonConfigs;
import cn.foggyhillside.ends_delight.registry.ModItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

public class DragonToothKnifeEvent {

    public static class KnifeEvents {
        public KnifeEvents() {
        }

        public static float onAttackEndMobs(LivingEntity livingEntity, DamageSource source, float amount) {
            String[] endMobs = EDCommonConfigs.END_MOBS.get().toArray(new String[0]);
            for (String endMob : endMobs) {
                Identifier id = Registries.ENTITY_TYPE.getId(livingEntity.getType());
                if (id.equals(Identifier.tryParse(endMob)) && source.getAttacker() instanceof LivingEntity attacker) {
                    ItemStack toolStack = attacker.getStackInHand(Hand.MAIN_HAND);
                    if (toolStack.isOf(ModItem.DragonToothKnife.get())) {
                        amount *= 3.5F;
                        break;
                    }
                }
            }
            return amount;
        }
    }
}