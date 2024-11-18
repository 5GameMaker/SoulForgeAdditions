package buj.soulforgeadditions.mixin;

import buj.soulforgeadditions.Globals;
import buj.soulforgeadditions.SoulForgeAdditions;
import com.pulsar.soulforge.networking.SoulForgeNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworking.class)
public class ClientPlayNetworkingMixin {
    @Inject(method = "send(Lnet/minecraft/util/Identifier;Lnet/minecraft/network/PacketByteBuf;)V", at = @At("HEAD"))
    private static void monitorPackets(Identifier channelName, PacketByteBuf buf, CallbackInfo ci) {
        if (channelName.equals(SoulForgeNetworking.SET_WEAPON) && SoulForgeAdditions.getConfig().autoSwitchToWeaponSlot) {
            Globals.TRY_SET_WEAPON_SLOT = true;
        }
    }
}
