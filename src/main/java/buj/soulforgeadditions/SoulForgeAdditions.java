package buj.soulforgeadditions;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import squeek.appleskin.client.HUDOverlayHandler;

public class SoulForgeAdditions implements ClientModInitializer {
    public static Logger LOG = LoggerFactory.getLogger("SoulForgeAdditions");

    @Override
    public void onInitializeClient() {
        if (FabricLoader.getInstance().isModLoaded("appleskin")) {
            if (HUDOverlayHandler.INSTANCE == null) {
                Globals.APPLESKIN_NOT_APPLIED = true;
                LOG.warn("Applying AppleSkin fix late!");
            }
            else HUDOverlayHandler.INSTANCE.FOOD_BAR_HEIGHT += 22;
        }

        if (FabricLoader.getInstance().isModLoaded("rei")) {
        }
    }
}
