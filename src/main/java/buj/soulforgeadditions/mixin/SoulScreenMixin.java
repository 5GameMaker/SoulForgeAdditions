package buj.soulforgeadditions.mixin;

import buj.soulforgeadditions.Globals;
import buj.soulforgeadditions.SoulForgeAdditions;
import com.pulsar.soulforge.SoulForge;
import com.pulsar.soulforge.ability.AbilityBase;
import com.pulsar.soulforge.client.ui.SoulScreen;
import com.pulsar.soulforge.components.SoulComponent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SoulScreen.class)
public abstract class SoulScreenMixin extends Screen {
    @Shadow(remap = false) public int x;
    @Shadow(remap = false) public int y;
    @Shadow(remap = false) public abstract void updateWidgets();
    @Shadow(remap = false) private AbilityBase selectedAbility;

    @Shadow protected abstract void init();

    protected SoulScreenMixin(Text title) {
        super(title);
    }

    @Redirect(method = "updateWidgets", at = @At(value = "INVOKE", target = "Lcom/pulsar/soulforge/ability/AbilityBase;getLocalizedText()Lnet/minecraft/text/Text;", shift = At.Shift.BY), require = 1)
    public Text overrideLocalizedText(AbilityBase instance) {
        Globals.STORE_ABILITY = instance;
        return Texts.join(List.of(
                instance.getLocalizedText(),
                Text.translatable(
                        "ability." + instance.getID().getPath() + ".summary"
                ).setStyle(Style.EMPTY.withColor(Formatting.GRAY))
        ), Text.of("\n\n"));
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIFFIIII)V", ordinal = 0, shift = At.Shift.AFTER))
    void renderDarkenedSlots(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        Globals.SELECTED_ABILITY = selectedAbility;

        assert client != null;
        SoulComponent soul = SoulForge.getPlayerSoul(client.player);

        if (soul != null) {
            final int row = soul.getAbilityRow();

            for (int r = 0; r < 4; r++) {
                if (r == row) continue;
                final int y = this.y + 101 + r * 18;
                context.fill(this.x + 17, y, this.x + 17 + 18 * 9, y + 18, 0x33000000);
            }
        }
    }

    @Override
    public boolean shouldPause() {
        return SoulForgeAdditions.getConfig().soulScreenPausesGame;
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawBorder(IIIII)V", ordinal = 0))
    void drawBorder(DrawContext instance, int x, int y, int width, int height, int color) {
        instance.drawTexture(ClickableWidget.WIDGETS_TEXTURE, x - 3, y - 3, 0, 22, 24, 24);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawBorder(IIIII)V", ordinal = 1))
    void drawBorderDontNeed(DrawContext instance, int x, int y, int width, int height, int color) {}
}
