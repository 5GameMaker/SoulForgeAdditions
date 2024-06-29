package buj.soulforgeadditions.mixin;

import buj.soulforgeadditions.Globals;
import com.pulsar.soulforge.SoulForge;
import com.pulsar.soulforge.ability.AbilityBase;
import com.pulsar.soulforge.components.SoulComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import squeek.appleskin.client.HUDOverlayHandler;

import java.util.Objects;
import java.util.logging.Logger;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow @Final private MinecraftClient client;
    @Shadow private ItemStack currentStack;
    @Shadow private int heldItemTooltipFade;

    @Shadow public abstract TextRenderer getTextRenderer();

    @Shadow private int scaledWidth;
    @Shadow private int scaledHeight;

    @Unique
    private @Nullable Text heldItemText = null;
    @Unique
    private int fadeTracker = 0;
    @Unique
    private boolean soulForgeHudActiveTracker = false;
    @Unique
    private int selectedSlotTracker = 0;

    /**
     * @author buj
     * @reason I need it to actually support my stuff
     */
    @Overwrite
    public void renderHeldItemTooltip(DrawContext context) {
        this.client.getProfiler().push("selectedItemName");
        if (this.heldItemTooltipFade > 0 && heldItemText != null) {
            int i = this.getTextRenderer().getWidth(heldItemText);
            int j = (this.scaledWidth - i) / 2;
            int k = this.scaledHeight - 59;

            assert this.client.interactionManager != null;
            if (!this.client.interactionManager.hasStatusBars()) {
                k += 14;
            }

            int l = (int)((float)this.heldItemTooltipFade * 256.0F / 10.0F);
            if (l > 255) {
                l = 255;
            }

            if (l > 0) {
                int var10001 = j - 2;
                int var10002 = k - 2;
                int var10003 = j + i + 2;
                Objects.requireNonNull(this.getTextRenderer());
                context.fill(var10001, var10002, var10003, k + 9 + 2, this.client.options.getTextBackgroundColor(0));
                context.drawTextWithShadow(this.getTextRenderer(), heldItemText, j, k, 16777215 + (l << 24));
            }
        }

        this.client.getProfiler().pop();
    }

    @Inject(method = "tick()V", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        if (Globals.APPLESKIN_NOT_APPLIED && HUDOverlayHandler.INSTANCE != null) {
            HUDOverlayHandler.INSTANCE.FOOD_BAR_HEIGHT += 22;
            Globals.APPLESKIN_NOT_APPLIED = false;
        }

        final boolean soulForgeHudActive;
        @Nullable SoulComponent soul = SoulForge.getPlayerSoul(client.player);
        if (soul == null) soulForgeHudActive = false;
        else soulForgeHudActive = soul.magicModeActive();

        if (soulForgeHudActive != soulForgeHudActiveTracker) {
            soulForgeHudActiveTracker = soulForgeHudActive;
            if (soulForgeHudActive) {
                AbilityBase ability = soul.getLayoutAbility(soul.getAbilityRow(), soul.getAbilitySlot());
                if (ability != null) {
                    Globals.NEW_HOTBAR_DISPLAY_TEXT = ability.getLocalizedText();
                }
            }
            else if (this.currentStack != null) {
                MutableText mutableText = Text.empty().append(this.currentStack.getName()).formatted(this.currentStack.getRarity().formatting);
                if (this.currentStack.hasCustomName()) {
                    mutableText.formatted(Formatting.ITALIC);
                }
                Globals.NEW_HOTBAR_DISPLAY_TEXT = mutableText;
            }
        }

        if (Globals.NEW_HOTBAR_DISPLAY_TEXT != null) {
            heldItemText = Globals.NEW_HOTBAR_DISPLAY_TEXT;
            this.heldItemTooltipFade = (int)(40.0 * this.client.options.getNotificationDisplayTime().getValue());
            fadeTracker = this.heldItemTooltipFade;

            Globals.NEW_HOTBAR_DISPLAY_TEXT = null;
        }

        if (this.heldItemTooltipFade <= fadeTracker) fadeTracker = this.heldItemTooltipFade;

        if (this.heldItemTooltipFade <= 0) {
            heldItemText = null;
        }
        else if (this.client.player != null && (heldItemText == null || fadeTracker < this.heldItemTooltipFade || this.client.player.getInventory().selectedSlot != selectedSlotTracker) && this.currentStack != null) {
            selectedSlotTracker = this.client.player.getInventory().selectedSlot;
            MutableText mutableText = Text.empty().append(this.currentStack.getName()).formatted(this.currentStack.getRarity().formatting);
            if (this.currentStack.hasCustomName()) {
                mutableText.formatted(Formatting.ITALIC);
            }
            heldItemText = mutableText;
            fadeTracker = this.heldItemTooltipFade;
        }
    }
}
