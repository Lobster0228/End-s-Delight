package cn.foggyhillside.ends_delight.registry;

import cn.foggyhillside.ends_delight.EndsDelight;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

public class DamageTypeRegistry {
    public static final ResourceKey<DamageType> ENDERMAN_GRISTLE_TELEPORT = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(EndsDelight.MODID, "enderman_gristle_teleport"));

    public static DamageSource getSimpleDamageSource(Level level, ResourceKey<DamageType> type) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(type));
    }

}
