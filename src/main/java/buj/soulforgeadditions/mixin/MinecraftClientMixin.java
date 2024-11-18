package buj.soulforgeadditions.mixin;

import buj.soulforgeadditions.Globals;
import buj.soulforgeadditions.SoulForgeAdditions;
import com.pulsar.soulforge.SoulForge;
import com.pulsar.soulforge.networking.SoulForgeNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow @Nullable public Screen currentScreen;

    @Shadow @Nullable public ClientPlayerEntity player;

    @Inject(method = "setScreen", at = @At("HEAD"))
    void setScreen(Screen screen, CallbackInfo ci) {
        Globals.PREVIOUS_SCREEN = currentScreen;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    void restoreWeapon(CallbackInfo ci) {
        if (!SoulForgeAdditions.getConfig().autoRestoreWeapon) return;
        if (Globals.RESTORE_TIME == 0) return;
        if (Globals.RESTORE_TIME > System.currentTimeMillis()) return;
        if (Globals.RESTORE_WEAPON_ABILITY == null) return;
        if (Globals.RESTORE_WEAPON == null) return;
        if (player == null) return;

        Globals.RESTORE_TIME = 0;
        ClientPlayNetworking.send(SoulForgeNetworking.SET_WEAPON, PacketByteBufs.create().writeItemStack(new ItemStack(Globals.RESTORE_WEAPON)));
    }

    @Inject(method = "tick", at = @At("HEAD"))
    void setCurrentSlot(CallbackInfo ci) {
        if (Globals.TRY_SET_WEAPON_SLOT && player != null && SoulForge.getPlayerSoul(player) != null
                && SoulForge.getPlayerSoul(player).hasWeapon()) {
            Globals.TRY_SET_WEAPON_SLOT = false;
            player.getInventory().selectedSlot = 9;
        }
    }
}
