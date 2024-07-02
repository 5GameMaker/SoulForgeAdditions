package buj.soulforgeadditions.mixin;

import buj.soulforgeadditions.Globals;
import buj.soulforgeadditions.SoulForgeAdditions;
import com.mojang.blaze3d.systems.RenderSystem;
import com.pulsar.soulforge.SoulForge;
import com.pulsar.soulforge.client.ui.MagicHudOverlay;
import com.pulsar.soulforge.components.SoulComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

import static org.lwjgl.opengl.GL20.*;

@Mixin(MagicHudOverlay.class)
public class MagicHudOverlayMixin {
    @Inject(method = "onHudRender", at = @At("HEAD"))
    void onHudRender(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (SoulForgeAdditions.getConfig().magicBarFade) {
            SoulComponent soul = SoulForge.getPlayerSoul(MinecraftClient.getInstance().player);
            if (soul != null) {
                if (soul.magicModeActive()) {
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
}
