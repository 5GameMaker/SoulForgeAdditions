package buj.soulforgeadditions.mixin;

import buj.soulforgeadditions.AbilitiesExt;
import com.mojang.blaze3d.systems.RenderSystem;
import com.pulsar.soulforge.SoulForge;
import com.pulsar.soulforge.ability.AbilityBase;
import com.pulsar.soulforge.components.SoulComponent;
import com.pulsar.soulforge.config.ConfigHelper;
import com.pulsar.soulforge.config.SoulForgeConfig;
import com.pulsar.soulforge.data.AbilityLayout;
import com.pulsar.soulforge.trait.TraitBase;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.*;
import net.minecraft.util.math.ColorHelper;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(value = InGameHud.class, priority = 999)
public abstract class InGameHudMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    private int scaledWidth;
    @Shadow
    private int scaledHeight;

    @Inject(method = "renderHotbar", at = @At("HEAD"))
    private void renderAbilityOverlay(float tickDelta, DrawContext context, CallbackInfo ci) {
        final boolean twoBars = ConfigHelper.getSplitHotbars();
        SoulComponent playerSoul = SoulForge.getPlayerSoul(client.player);
        if (playerSoul != null && (playerSoul.magicModeActive() || twoBars)) {
            AbilityLayout.AbilityRow row = playerSoul.getLayoutRow(playerSoul.getAbilityRow());
            int slot = playerSoul.getAbilitySlot();
            for (int i = 0; i < 9; i++) {
                AbilityBase ability = row.abilities.get(i);
                if (ability != null
                        && playerSoul.getActiveAbilities().stream().anyMatch(x -> x.getID().equals(ability.getID()))) {
                    int f2011 = this.scaledWidth / 2;

                    int n = f2011 - 90 + i * 20 + 2 - 2;
                    int o = scaledHeight - 16 - 3 - 2;
                    if (twoBars) o -= 22;

                    int[] colors = Arrays.stream(AbilitiesExt.getTraitsOf(ability))
                            .mapToInt(TraitBase::getColor)
                            .toArray();

                    if (colors.length == 0) return;

                    final int shiftPerColor = 20 / Math.max(colors.length, 1);
                    int shift = 0;

                    int n2 = n + 20;

                    boolean renderLeft = i - 1 != slot || !playerSoul.magicModeActive() || !twoBars;
                    boolean renderRight = i + 1 != slot || !playerSoul.magicModeActive() || !twoBars;

                    Matrix4f matrix = context.getMatrices().peek().getPositionMatrix();
                    BufferBuilder buffer = Tessellator.getInstance().getBuffer();
                    RenderSystem.setShader(GameRenderer::getPositionColorProgram);

                    buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

                    {
                        int color = colors[0];
                        float a = 1.0f;
                        float r = (float) ColorHelper.Argb.getRed(color) / 255.0f;
                        float g = (float) ColorHelper.Argb.getGreen(color) / 255.0f;
                        float b = (float) ColorHelper.Argb.getBlue(color) / 255.0f;

                        buffer.vertex(matrix, (float) n + 1, (float) o, 0).color(r, g, b, a).next();
                        buffer.vertex(matrix, (float) n + 1, (float) o + 1, 0).color(r, g, b, a).next();
                        buffer.vertex(matrix, (float) n2 - 1, (float) o + 1, 0).color(r, g, b, a).next();
                        buffer.vertex(matrix, (float) n2 - 1, (float) o, 0).color(r, g, b, a).next();
                    }

                    {
                        int color = colors[colors.length - 1];
                        float a = 1.0f;
                        float r = (float) ColorHelper.Argb.getRed(color) / 255.0f;
                        float g = (float) ColorHelper.Argb.getGreen(color) / 255.0f;
                        float b = (float) ColorHelper.Argb.getBlue(color) / 255.0f;

                        buffer.vertex(matrix, (float) n + 1, (float) o + 19, 0).color(r, g, b, a).next();
                        buffer.vertex(matrix, (float) n + 1, (float) o + 20, 0).color(r, g, b, a).next();
                        buffer.vertex(matrix, (float) n2 - 1, (float) o + 20, 0).color(r, g, b, a).next();
                        buffer.vertex(matrix, (float) n2 - 1, (float) o + 19, 0).color(r, g, b, a).next();
                    }

                    for (int color : colors) {
                        float a = 1.0f;
                        float r = (float) ColorHelper.Argb.getRed(color) / 255.0f;
                        float g = (float) ColorHelper.Argb.getGreen(color) / 255.0f;
                        float b = (float) ColorHelper.Argb.getBlue(color) / 255.0f;

                        if (renderLeft) {
                            buffer.vertex(matrix, (float) n, (float) o + shift, 0).color(r, g, b, a).next();
                            buffer.vertex(matrix, (float) n, (float) o + 20, 0).color(r, g, b, a).next();
                            buffer.vertex(matrix, (float) n + 1, (float) o + 20, 0).color(r, g, b, a).next();
                            buffer.vertex(matrix, (float) n + 1, (float) o + shift, 0).color(r, g, b, a).next();
                        }

                        if (renderRight) {
                            buffer.vertex(matrix, (float) n2 - 1, (float) o + shift, 0).color(r, g, b, a).next();
                            buffer.vertex(matrix, (float) n2 - 1, (float) o + 20, 0).color(r, g, b, a).next();
                            buffer.vertex(matrix, (float) n2, (float) o + 20, 0).color(r, g, b, a).next();
                            buffer.vertex(matrix, (float) n2, (float) o + shift, 0).color(r, g, b, a).next();
                        }

                        shift += shiftPerColor;
                    }

                    BufferRenderer.drawWithGlobalProgram(buffer.end());
                }
            }
        }
    }
}
