package cn.foggyhillside.endsdelight.item;

import cn.foggyhillside.endsdelight.EndsDelight;
import cn.foggyhillside.endsdelight.config.EDCommonConfigs;
import cn.foggyhillside.endsdelight.registry.ItemRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class DragonToothKnifeEvent {

    @Mod.EventBusSubscriber(
            modid = EndsDelight.MOD_ID,
            bus = Mod.EventBusSubscriber.Bus.FORGE
    )
    public static class KnifeEvents {
        public KnifeEvents() {
        }

        @SubscribeEvent
        public static void onAttackEndMobs(LivingDamageEvent event) {
            LivingEntity target = event.getEntity();
            String[] endMobs = EDCommonConfigs.END_MOBS.get().toArray(new String[0]);
            for (String endMob : endMobs) {
                ResourceLocation id = Registry.ENTITY_TYPE.getKey(target.getType());
                if (id.equals(ResourceLocation.tryParse(endMob)) && event.getSource().getEntity() instanceof LivingEntity attacker) {
                    ItemStack toolStack = attacker.getItemInHand(InteractionHand.MAIN_HAND);
                    if (toolStack.is(ItemRegistry.DragonToothKnife.get())) {
                        event.setAmount(event.getAmount() * 3.5F);
                        break;
                    }
                }

            }
        }
    }
}
