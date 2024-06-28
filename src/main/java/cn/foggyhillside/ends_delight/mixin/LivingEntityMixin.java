package cn.foggyhillside.ends_delight.mixin;

import cn.foggyhillside.ends_delight.item.DragonToothKnifeEvent;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin({LivingEntity.class})
public abstract class LivingEntityMixin extends Entity implements Attackable {

    @Shadow
    public abstract float getHealth();

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyArg(
            method = {"applyDamage"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;setHealth(F)V"
            ),
            index = 0
    )
    protected float setHealthMixin(float amount, @Local DamageSource source) {
        return this.getHealth() - DragonToothKnifeEvent.KnifeEvents.onAttackEndMobs((LivingEntity) (Object) this, source, this.getHealth() - amount);
    }

    @ModifyArg(
            method = {"applyDamage"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/damage/DamageTracker;onDamage(Lnet/minecraft/entity/damage/DamageSource;F)V"
            ),
            index = 1
    )
    protected float onDamageMixin(float amount, @Local DamageSource source) {
        return this.getHealth() - DragonToothKnifeEvent.KnifeEvents.onAttackEndMobs((LivingEntity) (Object) this, source, this.getHealth() - amount);
    }

    @ModifyArg(
            method = {"applyDamage"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;setAbsorptionAmount(F)V"
            ),
            index = 0
    )
    protected float setAbsorptionAmountMixin(float amount, @Local DamageSource source) {
        return this.getHealth() - DragonToothKnifeEvent.KnifeEvents.onAttackEndMobs((LivingEntity) (Object) this, source, this.getHealth() - amount);
    }

}
