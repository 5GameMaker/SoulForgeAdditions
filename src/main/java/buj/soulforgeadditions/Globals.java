package buj.soulforgeadditions;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class Globals {
    private Globals() {}

    public static int DISPLAY_SLOT = -1;
    public static int DISPLAY_ROW = -1;

    public static @Nullable Text NEW_HOTBAR_DISPLAY_TEXT = null;

    public static float MAGIC_HUD_OPACITY_ANIM = 0;

    public static @Nullable Screen PREVIOUS_SCREEN = null;

    public static boolean DEFERRED_CHANGE_SLOT = false;
}
