package cn.foggyhillside.ends_delight.registry;

import cn.foggyhillside.ends_delight.EndsDelight;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class EDModDamageTypes {
    public static final RegistryKey<DamageType> ENDERMAN_GRISTLE_TELEPORT = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(EndsDelight.MOD_ID, "enderman_gristle_teleport"));

}
