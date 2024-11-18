package buj.soulforgeadditions.mixin;

import buj.soulforgeadditions.Globals;
import com.pulsar.soulforge.client.ui.MorphingWeaponryScreen;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MorphingWeaponryScreen.class)
public class MorphingWeaponryScreenMixin {
    @Shadow private ItemStack hovering;

    @Inject(method = "close", at = @At("HEAD"))
    private void applyRestoreWeapon(CallbackInfo ci) {
        if (hovering != null) {
            Globals.RESTORE_WEAPON = hovering.getItem();
            Globals.RESTORE_WEAPON_ABILITY = "Morphing Weaponry";
        }
    }
}
