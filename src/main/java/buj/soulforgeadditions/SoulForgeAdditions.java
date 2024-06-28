package buj.soulforgeadditions;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import squeek.appleskin.client.HUDOverlayHandler;

public class SoulForgeAdditions implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        if (FabricLoader.getInstance().isModLoaded("appleskin")) {
            HUDOverlayHandler.INSTANCE.FOOD_BAR_HEIGHT += 22;
        }

        if (FabricLoader.getInstance().isModLoaded("rei")) {
        }
    }
}
