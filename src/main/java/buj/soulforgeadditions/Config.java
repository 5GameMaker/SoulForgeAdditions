package buj.soulforgeadditions;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;

public class Config {
    public boolean magicBarFade = true;
    public boolean soulScreenPausesGame = false;
    public boolean scrollChangesRows = true;
    public boolean displayManaCost = true;
    public boolean autoSwitchToWeaponSlot = true;
    public boolean autoRestoreWeapon = true;

    @me.shedaniel.autoconfig.annotation.Config(name = "soulforgeadditions")
    public static class ConfigModel implements ConfigData {
        public boolean magicBarFade = true;
        public boolean soulScreenPausesGame = false;
        public boolean scrollChangesRows = true;
        public boolean displayManaCost = true;
        public boolean autoSwitchToWeaponSlot = true;
        public boolean autoRestoreWeapon = true;
    }

    public static Config cloth() {
        Config config = new Config();
        ConfigModel model = AutoConfig.getConfigHolder(ConfigModel.class).getConfig();
        config.magicBarFade = model.magicBarFade;
        config.soulScreenPausesGame = model.soulScreenPausesGame;
        config.scrollChangesRows = model.scrollChangesRows;
        config.displayManaCost = model.displayManaCost;
        config.autoSwitchToWeaponSlot = model.autoSwitchToWeaponSlot;
        config.autoRestoreWeapon = model.autoRestoreWeapon;

        return config;
    }
}
