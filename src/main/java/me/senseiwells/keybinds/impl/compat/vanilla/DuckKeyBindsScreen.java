package me.senseiwells.keybinds.impl.compat.vanilla;

import me.senseiwells.keybinds.api.Keybind;
import org.jetbrains.annotations.Nullable;

public interface DuckKeyBindsScreen {
	void skl$setKeybind(@Nullable Keybind keybind);

	@Nullable Keybind skl$getKeybind();
}
