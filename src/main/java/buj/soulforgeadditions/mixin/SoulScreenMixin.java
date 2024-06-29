package buj.soulforgeadditions.mixin;

import com.pulsar.soulforge.ability.AbilityBase;
import com.pulsar.soulforge.client.ui.SoulScreen;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(SoulScreen.class)
public class SoulScreenMixin {
    @Redirect(method = "updateWidgets", at = @At(value = "INVOKE", target = "Lcom/pulsar/soulforge/ability/AbilityBase;getLocalizedText()Lnet/minecraft/text/Text;", shift = At.Shift.BY), require = 1)
    public Text overrideLocalizedText(AbilityBase instance) {
        return Texts.join(List.of(
                instance.getLocalizedText(),
                Text.translatable(
                        "ability." + instance.getID().getPath() + ".summary"
                ).setStyle(Style.EMPTY.withColor(Formatting.GRAY))
        ), Text.of("\n\n"));
    }
}
