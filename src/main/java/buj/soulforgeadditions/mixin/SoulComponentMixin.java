package buj.soulforgeadditions.mixin;

import buj.soulforgeadditions.Globals;
import buj.soulforgeadditions.SoulForgeAdditions;
import com.pulsar.soulforge.components.SoulComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoulComponent.class)
public class SoulComponentMixin {
    @Shadow @Final private PlayerEntity player;

    @Inject(method = "setWeapon(Lnet/minecraft/item/ItemStack;Z)V", at = @At("TAIL"))
    public void setWeaponFinalize(ItemStack weapon, boolean sound, CallbackInfo ci) {
        if (player == null) return;
        if (!SoulForgeAdditions.getConfig().autoSwitchToWeaponSlot) return;

        Globals.TRY_SET_WEAPON_SLOT = true;
    }
}
