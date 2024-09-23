package buj.soulforgeadditions.mixin;

import buj.soulforgeadditions.Globals;
import buj.soulforgeadditions.SoulForgeAdditions;
import com.mojang.blaze3d.systems.RenderSystem;
import com.pulsar.soulforge.SoulForge;
import com.pulsar.soulforge.ability.AbilityBase;
import com.pulsar.soulforge.ability.ToggleableAbilityBase;
import com.pulsar.soulforge.attribute.SoulForgeAttributes;
import com.pulsar.soulforge.client.ui.MagicHudOverlay;
import com.pulsar.soulforge.client.ui.SoulScreen;
import com.pulsar.soulforge.components.SoulComponent;
import com.pulsar.soulforge.config.SoulForgeConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;
import org.lwjgl.system.MathUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.lang.reflect.Field;

import static org.lwjgl.opengl.GL20.*;

@Mixin(MagicHudOverlay.class)
public class MagicHudOverlayMixin {
    @Inject(method = "onHudRender", at = @At("HEAD"))
    void onHudRender(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (SoulForgeAdditions.getConfig().magicBarFade) {
            MinecraftClient client = MinecraftClient.getInstance();
            SoulComponent soul = SoulForge.getPlayerSoul(client.player);
            if (soul != null) {
                if (soul.magicModeActive() || client.currentScreen instanceof SoulScreen) {
                    Globals.MAGIC_HUD_OPACITY_ANIM -= tickDelta / 10;
                    if (Globals.MAGIC_HUD_OPACITY_ANIM < 0) Globals.MAGIC_HUD_OPACITY_ANIM = 0;
                }
                else if (soul.getMagic() < 99.99f) {
                    Globals.MAGIC_HUD_OPACITY_ANIM += tickDelta / 40;
                    if (Globals.MAGIC_HUD_OPACITY_ANIM > 1.5f) Globals.MAGIC_HUD_OPACITY_ANIM = 1.5f;
                }
                else {
                    Globals.MAGIC_HUD_OPACITY_ANIM += tickDelta / 40;
                    if (Globals.MAGIC_HUD_OPACITY_ANIM > 2) Globals.MAGIC_HUD_OPACITY_ANIM = 2;
                }
            }
            else Globals.MAGIC_HUD_OPACITY_ANIM = 0;
        }
        else Globals.MAGIC_HUD_OPACITY_ANIM = 0;
    }

    @Redirect(method = "renderMagicBar", at = @At(value = "INVOKE", target = "Ljava/awt/Color;getRGB()I"), remap = false)
    public int getRGB(Color instance) {
        int alpha = 255;
        if (Globals.MAGIC_HUD_OPACITY_ANIM > 1f) {
            alpha = 255 - (int) ((Globals.MAGIC_HUD_OPACITY_ANIM - 1f) * 255);
        }
        return new Color(instance.getRed(), instance.getGreen(), instance.getBlue(), alpha).getRGB();
    }

    @Redirect(
            method = "renderMagicBar",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIFFIIII)V"
            )
    )
    public void drawTexture(
            DrawContext instance, Identifier texture,
            int x, int y, float u, float v,
            int width, int height,
            int textureWidth, int textureHeight) {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        if (Globals.MAGIC_HUD_OPACITY_ANIM > 1f) {
            RenderSystem.setShaderColor(1f, 1f, 1f, 2 - Globals.MAGIC_HUD_OPACITY_ANIM);
        }
        instance.drawTexture(texture, x, y, u, v, width, height, textureWidth, textureHeight);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.disableBlend();
    }

    @Inject(method = "renderMagicBar", at = @At("TAIL"))
    void renderBlackBox(DrawContext context, CallbackInfo ci) {
        if (!SoulForgeAdditions.getConfig().displayManaCost) return;

        MinecraftClient client = MinecraftClient.getInstance();
        SoulComponent playerSoul = SoulForge.getPlayerSoul(client.player);
        Screen screen = client.currentScreen;
        if (!playerSoul.magicModeActive() && !(screen instanceof SoulScreen)) return;
        AbilityBase ability;
        if (screen == null)
            ability = playerSoul.getAbilityLayout().getSlot(playerSoul.getAbilityRow(), playerSoul.getAbilitySlot());
        else if (screen instanceof SoulScreen) {
            ability = Globals.SELECTED_ABILITY;
            if (ability == null) ability = Globals.HOVERED_ABILITY;
        }
        else return;
        if (ability == null) return;
        double alpha = Math.sin((double) (System.currentTimeMillis() % 4000) / 2000f * Math.PI) / 2 + 0.6;

        int top;
        int left = switch (SoulForgeConfig.MAGIC_BAR_LOCATION.getValue()) {
            case BOTTOM_RIGHT -> {
                top = client.getWindow().getScaledHeight() - 136;
                yield client.getWindow().getScaledWidth() - 27;
            }
            case TOP_LEFT -> {
                top = 30;
                yield 5;
            }
            case TOP_RIGHT -> {
                top = 30;
                yield client.getWindow().getScaledWidth() - 27;
            }
            default -> {
                top = client.getWindow().getScaledHeight() - 136;
                yield 5;
            }
        };

        int cost = ability.getCost();
        if (client.player.getAttributeInstance(SoulForgeAttributes.MAGIC_COST) != null)
            cost = (int) Math.ceil(cost * client.player.getAttributeValue(SoulForgeAttributes.MAGIC_COST));
        if (playerSoul.isStrong()) cost /= 2;
        if (ability instanceof ToggleableAbilityBase) cost = 100;
        int present = (int) playerSoul.getMagic();
        if (present <= 50) present--; // bleh

        if (cost > present) return;
        cost = Math.min(cost, 100);

        Globals.HOVERED_ABILITY = null;
        Color color = new Color(0, 0, 0, (float) alpha / 3);
        context.fill(left + 3, top + 103 - present + cost, left + 13, top + 103 - present, color.getRGB());
    }
}
