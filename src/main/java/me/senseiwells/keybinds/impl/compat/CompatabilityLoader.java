package me.senseiwells.keybinds.impl.compat;

import me.senseiwells.keybinds.impl.compat.yacl.YACLKeybindings;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class CompatabilityLoader {
	public static void load() {
		if (FabricLoader.getInstance().isModLoaded("yet_another_config_lib_v3")) {
			YACLKeybindings.load();
		}
	}
}
