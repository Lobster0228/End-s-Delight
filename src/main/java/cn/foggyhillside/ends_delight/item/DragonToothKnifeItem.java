package cn.foggyhillside.ends_delight.item;

import cn.foggyhillside.ends_delight.EndsDelight;
import cn.foggyhillside.ends_delight.config.EDCommonConfigs;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.common.item.KnifeItem;

public class DragonToothKnifeItem extends KnifeItem {
    public DragonToothKnifeItem(Tier tier, float attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }

    @Mod.EventBusSubscriber(
            modid = EndsDelight.MODID,
            bus = Mod.EventBusSubscriber.Bus.FORGE
    )
    public static class KnifeEvents {
        public KnifeEvents() {
        }

        @SubscribeEvent
        public static void onAttackEndMobs(LivingDamageEvent event) {
            LivingEntity attacker = event.getEntity().getKillCredit();
            LivingEntity target = event.getEntity();
            ItemStack toolStack = attacker != null ? attacker.getItemInHand(InteractionHand.MAIN_HAND) : ItemStack.EMPTY;
            String[] endMobs = EDCommonConfigs.END_MOBS.get().toArray(new String[0]);
            for (String endMob : endMobs) {
                String[] split = target.getType().toString().split("\\.");
                if (split[1] != null && split[2] != null) {
                    String id = String.join(":", split[1], split[2]);
                    if (id.equals(endMob) &&
                            toolStack.getItem() instanceof DragonToothKnifeItem) {
                        event.setAmount(event.getAmount() * 3.5F);
                        break;
                    }
                }

            }

        }

    }
}
