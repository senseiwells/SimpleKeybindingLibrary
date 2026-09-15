package me.senseiwells.keybinds.impl.util;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import it.unimi.dsi.fastutil.ints.IntList;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class KeybindingUtils {
	public static final IntList ESCAPE_KEYS = IntList.of(
		InputConstants.KEY_NUMPADENTER, InputConstants.KEY_RETURN, InputConstants.KEY_ESCAPE
	);

	private static final Codec<InputConstants.Key> KEY_NAME_CODEC = Codec.STRING.comapFlatMap(
		KeybindingUtils::fromName, InputConstants.Key::getName
	);

	public static final Codec<InputConstants.Key> KEY_CODEC = Codec.withAlternative(KEY_NAME_CODEC, LegacyKeys.CODEC);

	private KeybindingUtils() {

	}

	private static DataResult<InputConstants.Key> fromName(String name) {
		try {
			return DataResult.success(InputConstants.getKey(name));
		} catch (IllegalArgumentException | IndexOutOfBoundsException e) {
			return DataResult.error(() -> "Unknown key name: %s".formatted(name));
		}
	}
}
