package buj.soulforgeadditions.mixin;

import buj.soulforgeadditions.Globals;
import buj.soulforgeadditions.SoulForgeAdditions;
import com.pulsar.soulforge.client.ui.EncyclopediaScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(EncyclopediaScreen.class)
public class EncyclopediaScreenMixin extends Screen {
    @Unique
    public @Nullable Screen previousScreen;

    protected EncyclopediaScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("HEAD"))
    void saveScreen(CallbackInfo ci) {
        previousScreen = Globals.PREVIOUS_SCREEN;
    }

    @Override
    public boolean shouldPause() {
        return SoulForgeAdditions.getConfig().soulScreenPausesGame;
    }

    @Override
    public void close() {
        assert client != null;
        client.setScreen(previousScreen);
    }
}
