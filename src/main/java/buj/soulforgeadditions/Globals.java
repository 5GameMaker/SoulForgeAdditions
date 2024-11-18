package buj.soulforgeadditions;

import com.pulsar.soulforge.ability.AbilityBase;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import org.jetbrains.annotations.Nullable;

public class Globals {
    private Globals() {
    }

    public static float MAGIC_HUD_OPACITY_ANIM = 0;

    public static AbilityBase STORE_ABILITY = null;
    public static AbilityBase SELECTED_ABILITY = null;
    public static AbilityBase HOVERED_ABILITY = null;

    public static @Nullable Screen PREVIOUS_SCREEN = null;

    public static long RESTORE_TIME = 0;
    public static @Nullable Item RESTORE_WEAPON = null;
    public static @Nullable String RESTORE_WEAPON_ABILITY = null;

    public static boolean TRY_SET_WEAPON_SLOT = false;
}
