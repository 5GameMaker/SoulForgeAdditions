package buj.soulforgeadditions;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoulForgeAdditions implements ClientModInitializer {
    public static Logger LOG = LoggerFactory.getLogger("SoulForgeAdditions");

    private static Config CONFIG = null;

    @Override
    public void onInitializeClient() {
        if (FabricLoader.getInstance().isModLoaded("cloth-config")) {
            AutoConfig.register(Config.class, GsonConfigSerializer::new);
        }
    }

    public static Config getConfig() {
        if (FabricLoader.getInstance().isModLoaded("cloth-config")) {
            return AutoConfig.getConfigHolder(Config.class).getConfig();
        }

        if (CONFIG == null) CONFIG = new Config();
        return CONFIG;
    }
}
