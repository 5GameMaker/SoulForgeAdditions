package buj.soulforgeadditions.mixin;

import buj.soulforgeadditions.Globals;
import buj.soulforgeadditions.SoulForgeAdditions;
import com.pulsar.soulforge.SoulForge;
import com.pulsar.soulforge.components.SoulComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "onPlayerRespawn", at = @At("TAIL"))
    public void restoreWeapon(PlayerRespawnS2CPacket packet, CallbackInfo ci) {
        SoulComponent soul;

        if (!SoulForgeAdditions.getConfig().autoRestoreWeapon) return;
        if (Globals.RESTORE_WEAPON_ABILITY == null) return;
        if (Globals.RESTORE_WEAPON == null) return;
        if (client.player == null) return;
        if ((soul = SoulForge.getPlayerSoul(client.player)) == null) return;
        if (!soul.hasAbility(Globals.RESTORE_WEAPON_ABILITY)) return;

        Globals.RESTORE_TIME = System.currentTimeMillis() + 50;
    }
}
