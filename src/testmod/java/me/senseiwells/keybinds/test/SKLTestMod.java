package me.senseiwells.keybinds.test;

import com.mojang.blaze3d.platform.InputConstants;
import me.senseiwells.keybinds.api.InputKeys;
import me.senseiwells.keybinds.api.Keybind;
import me.senseiwells.keybinds.api.KeybindManager;
import me.senseiwells.keybinds.impl.SimpleKeybindingLibrary;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.KeyMapping;

public class SKLTestMod implements ModInitializer {
    private static final Keybind TEST = KeybindManager.register(
        SimpleKeybindingLibrary.id("test"), InputKeys.of(InputConstants.KEY_F6)
    );
    private static final Keybind TOGGLE_TEST = KeybindManager.register(
        SimpleKeybindingLibrary.id("toggle_test"), InputKeys.of(InputConstants.KEY_F7)
    );

    @Override
    public void onInitialize() {
        KeybindManager.addToControlsScreen(KeyMapping.Category.DEBUG, TEST);
        KeybindManager.addToControlsScreen(KeyMapping.Category.DEBUG, TOGGLE_TEST);
    }
}
