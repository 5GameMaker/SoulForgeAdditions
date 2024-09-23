package buj.soulforgeadditions.mixin;

import buj.soulforgeadditions.Globals;
import buj.soulforgeadditions.SoulForgeAdditions;
import com.pulsar.soulforge.ability.AbilityBase;
import com.pulsar.soulforge.client.ui.SoulScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoulScreen.ClickableTextureWidget.class)
public abstract class ClickableTextureWidgetMixin extends ClickableWidget {
    private AbilityBase storedHoverAbility = null;

    public ClickableTextureWidgetMixin(int x, int y, int width, int height, Text message) {
        super(x, y, width, height, message);
    }

    @Inject(method = "<init>(IIIILnet/minecraft/util/Identifier;Lcom/pulsar/soulforge/client/ui/SoulScreen$ClickableTextureWidget$PressAction;)V", at = @At("TAIL"))
    void init(int x, int y, int width, int height, Identifier texture, SoulScreen.ClickableTextureWidget.PressAction pressAction, CallbackInfo ci) {
        storedHoverAbility = Globals.STORE_ABILITY;
        Globals.STORE_ABILITY = null;
    }

    @Inject(method = "renderButton", at = @At("HEAD"))
    protected void renderButton(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (this.hovered) Globals.HOVERED_ABILITY = storedHoverAbility;
    }
}
