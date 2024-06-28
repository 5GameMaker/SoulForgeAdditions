package buj.soulforgeadditions;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.entry.EntryRegistry;

public class REIIntegration implements REIClientPlugin {
    @Override
    public void registerEntries(EntryRegistry registry) {
        REIClientPlugin.super.registerEntries(registry);
    }
}
