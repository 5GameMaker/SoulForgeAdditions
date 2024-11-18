package buj.soulforgeadditions.mixin;

import buj.soulforgeadditions.Globals;
import buj.soulforgeadditions.SoulForgeAdditions;
import com.pulsar.soulforge.ability.AbilityBase;
import com.pulsar.soulforge.ability.WeaponAbilityBase;
import net.minecraft.item.Item;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WeaponAbilityBase.class)
public abstract class WeaponAbilityBaseMixin extends AbilityBase {
    @Shadow public abstract Item getItem();

    @Inject(method = "cast", at = @At("RETURN"))
    private void setRestoreWeapon(ServerPlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        Globals.RESTORE_WEAPON_ABILITY = this.getName();
        Globals.RESTORE_WEAPON = this.getItem();

        SoulForgeAdditions.LOG.info("Weapon: {}", Globals.RESTORE_WEAPON_ABILITY);
    }
}
