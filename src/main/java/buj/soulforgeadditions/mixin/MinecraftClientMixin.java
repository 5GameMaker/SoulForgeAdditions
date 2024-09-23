package buj.soulforgeadditions.mixin;

import buj.soulforgeadditions.Globals;
import buj.soulforgeadditions.SoulForgeAdditions;
import com.pulsar.soulforge.SoulForge;
import com.pulsar.soulforge.components.SoulComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerInventory;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow @Nullable public Screen currentScreen;

    @Inject(method = "setScreen", at = @At("HEAD"))
    void setScreen(Screen screen, CallbackInfo ci) {
        Globals.PREVIOUS_SCREEN = currentScreen;
    }

    //@Redirect(method = "handleInputEvents", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerInventory;selectedSlot:I", opcode = Opcodes.PUTFIELD))
    //void setSelectedSlot(PlayerInventory inventory, int slot) {
    //    SoulComponent component = SoulForge.getPlayerSoul(inventory.player);
    //    if (component != null && component.magicModeActive() && SoulForgeAdditions.getConfig().numbersChangeMagicBarSlot) {
    //        component.setAbilitySlot(slot);
    //    }
    //    else inventory.selectedSlot = slot;
    //}
}
