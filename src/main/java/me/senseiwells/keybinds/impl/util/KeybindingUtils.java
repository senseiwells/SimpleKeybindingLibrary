package me.senseiwells.keybinds.impl.util;

import com.mojang.blaze3d.platform.InputConstants;
import it.unimi.dsi.fastutil.ints.IntList;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class KeybindingUtils {
	public static final IntList ESCAPE_KEYS = IntList.of(
		InputConstants.KEY_NUMPADENTER, InputConstants.KEY_RETURN, InputConstants.KEY_ESCAPE
	);

	private KeybindingUtils() {

	}
}
