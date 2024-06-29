package buj.soulforgeadditions.mixin;

import buj.soulforgeadditions.Globals;
import com.pulsar.soulforge.ability.AbilityBase;
import com.pulsar.soulforge.components.PlayerSoulComponent;
import com.pulsar.soulforge.components.SoulComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerSoulComponent.class)
public abstract class PlayerSoulComponentMixin implements SoulComponent {
    @Shadow @Final private PlayerEntity player;

    @Shadow public abstract int getAbilityRow();

    @Shadow public abstract int getAbilitySlot();

    @Inject(method = "setAbilitySlot(I)V", at = @At("TAIL"), remap = false)
    public void setAbilitySlot(int i, CallbackInfo ci) {
        int slot = this.getAbilitySlot();
        int row = this.getAbilityRow();

        if (this.player == MinecraftClient.getInstance().player) {
            @Nullable AbilityBase ability = this.getLayoutAbility(row, slot);
            if (ability != null) {
                if (Globals.DISPLAY_SLOT != slot || Globals.DISPLAY_ROW != row) {
                    Globals.NEW_HOTBAR_DISPLAY_TEXT = ability.getLocalizedText();
                    Globals.DISPLAY_SLOT = slot;
                    Globals.DISPLAY_ROW = row;
                }
            }
            else {
                Globals.DISPLAY_SLOT = -1;
                Globals.DISPLAY_ROW = -1;
            }
        }
    }
}
