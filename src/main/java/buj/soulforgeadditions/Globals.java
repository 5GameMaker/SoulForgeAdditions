package buj.soulforgeadditions;

import com.pulsar.soulforge.ability.AbilityBase;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;

public class Globals {
    private Globals() {
    }

    public static float MAGIC_HUD_OPACITY_ANIM = 0;

    public static AbilityBase STORE_ABILITY = null;
    public static AbilityBase SELECTED_ABILITY = null;
    public static AbilityBase HOVERED_ABILITY = null;

    public static @Nullable Screen PREVIOUS_SCREEN = null;
}
