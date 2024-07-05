package buj.soulforgeadditions.mixin;

import buj.soulforgeadditions.SoulForgeAdditions;
import com.pulsar.soulforge.SoulForge;
import com.pulsar.soulforge.components.SoulComponent;
import com.pulsar.soulforge.networking.SoulForgeNetworking;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = PlayerInventory.class, priority = 999)
public class PlayerInventoryMixin {
    @Dynamic
    @Shadow
    @Final
    public PlayerEntity field_7546;

    @Inject(method = "scrollInHotbar", at = @At("HEAD"))
    protected void handleHotbarScroll(double scrollAmount, CallbackInfo ci) {
        PlayerEntity player = this.field_7546;
        if (player.getWorld().isClient && SoulForgeAdditions.getConfig().scrollChangesRows) {
            SoulComponent playerSoul = SoulForge.getPlayerSoul(player);
            int direction = (int) Math.signum(scrollAmount);

            if (playerSoul != null && playerSoul.magicModeActive()) {
                if (playerSoul.getAbilitySlot() - direction < 0) {
                    playerSoul.setAbilityRow((playerSoul.getAbilityRow() + 3) % 4);
                    ClientPlayNetworking.send(SoulForgeNetworking.SWITCH_MODE, PacketByteBufs.create().writeVarInt(-1));
                } else if (playerSoul.getAbilitySlot() - direction > 8) {
                    playerSoul.setAbilityRow((playerSoul.getAbilityRow() + 1) % 4);
                    ClientPlayNetworking.send(SoulForgeNetworking.SWITCH_MODE, PacketByteBufs.create().writeVarInt(1));
                }
            }
        }
    }
}
