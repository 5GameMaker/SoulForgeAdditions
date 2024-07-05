package buj.soulforgeadditions.mixin;

import buj.soulforgeadditions.Globals;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow @Nullable public Screen currentScreen;

    @Inject(method = "setScreen", at = @At("HEAD"))
    void setScreen(Screen screen, CallbackInfo ci) {
        Globals.PREVIOUS_SCREEN = currentScreen;
    }
}
