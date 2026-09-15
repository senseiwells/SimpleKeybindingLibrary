package me.senseiwells.keybinds.impl.util;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class KeybindingUtils {
	public static final IntList ESCAPE_KEYS = IntList.of(
		InputConstants.KEY_NUMPADENTER, InputConstants.KEY_RETURN, InputConstants.KEY_ESCAPE
	);

	private static final Codec<InputConstants.Type> KEY_TYPE_CODEC = Codec.STRING.comapFlatMap(
        KeybindingUtils::fromString, KeybindingUtils::toString
	);

	public static final Codec<InputConstants.Key> KEY_CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
			KEY_TYPE_CODEC.fieldOf("type").forGetter(InputConstants.Key::getType),
			Codec.INT.fieldOf("code").forGetter(InputConstants.Key::getValue)
		).apply(instance, InputConstants.Type::getOrCreate);
	});

	private KeybindingUtils() {

	}

	private static DataResult<InputConstants.Type> fromString(String string) {
		return switch (string.toLowerCase()) {
			case "keyboard", "keysym" -> DataResult.success(InputConstants.Type.KEYBOARD);
			case "mouse" -> DataResult.success(InputConstants.Type.MOUSE);
            default -> DataResult.error(() -> "Unknown input type: %s".formatted(string));
		};
	}

	private static String toString(InputConstants.Type type) {
		return type.name().toLowerCase();
	}
}
