package buj.soulforgeadditions.mixin;

import com.pulsar.soulforge.ability.AbilityBase;
import com.pulsar.soulforge.client.ui.SoulScreen;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SoulScreen.class)
public class SoulScreenBoxHotfixMixin {
    @Shadow(remap = false) private AbilityBase selectedAbility;
    @Shadow(remap = false) private int page;

    @Redirect(method = "render", at = @At(value = "FIELD", target = "Lcom/pulsar/soulforge/client/ui/SoulScreen;selectedAbility:Lcom/pulsar/soulforge/ability/AbilityBase;", opcode = Opcodes.GETFIELD, ordinal = 1))
    AbilityBase filteredSelectedAbility(SoulScreen instance) {
        return this.page == 0 ? this.selectedAbility : null;
    }
}
