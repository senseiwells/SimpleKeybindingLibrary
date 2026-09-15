package me.senseiwells.keybinds.impl.util;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class LegacyKeys {
	private static final Int2ObjectMap<String> GLFW_KEYBOARD = new Int2ObjectOpenHashMap<>();
	private static final Int2ObjectMap<String> GLFW_MOUSE = new Int2ObjectOpenHashMap<>();

	private static final Codec<LegacyKey> LEGACY_CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
			Codec.STRING.fieldOf("type").forGetter(LegacyKey::type),
			Codec.INT.fieldOf("code").forGetter(LegacyKey::code)
		).apply(instance, LegacyKey::new);
	});

	public static final Codec<InputConstants.Key> CODEC = LEGACY_CODEC.flatXmap(
		LegacyKey::toKey, _ -> DataResult.error(() -> "Legacy key format is read-only")
	);

	private LegacyKeys() {

	}

	private static InputConstants.Key lookup(Int2ObjectMap<String> table, int code) {
		String name = table.get(code);
		return name == null ? InputConstants.UNKNOWN : InputConstants.getKey(name);
	}

	private record LegacyKey(String type, int code) {
		DataResult<InputConstants.Key> toKey() {
			return switch (this.type) {
				case "KEYSYM" -> DataResult.success(lookup(GLFW_KEYBOARD, this.code));
				case "MOUSE" -> DataResult.success(lookup(GLFW_MOUSE, this.code));
				case "SCANCODE" -> DataResult.success(InputConstants.UNKNOWN);
				default -> DataResult.error(() -> "Unknown legacy input type: %s".formatted(this.type));
			};
		}
	}

	static {
		GLFW_MOUSE.put(0, "key.mouse.left");
		GLFW_MOUSE.put(1, "key.mouse.right");
		GLFW_MOUSE.put(2, "key.mouse.middle");
		GLFW_MOUSE.put(3, "key.mouse.4");
		GLFW_MOUSE.put(4, "key.mouse.5");
		GLFW_MOUSE.put(5, "key.mouse.6");
		GLFW_MOUSE.put(6, "key.mouse.7");
		GLFW_MOUSE.put(7, "key.mouse.8");

		GLFW_KEYBOARD.put(-1, "key.keyboard.unknown");
		GLFW_KEYBOARD.put(32, "key.keyboard.space");
		GLFW_KEYBOARD.put(39, "key.keyboard.apostrophe");
		GLFW_KEYBOARD.put(44, "key.keyboard.comma");
		GLFW_KEYBOARD.put(45, "key.keyboard.minus");
		GLFW_KEYBOARD.put(46, "key.keyboard.period");
		GLFW_KEYBOARD.put(47, "key.keyboard.slash");
		GLFW_KEYBOARD.put(48, "key.keyboard.0");
		GLFW_KEYBOARD.put(49, "key.keyboard.1");
		GLFW_KEYBOARD.put(50, "key.keyboard.2");
		GLFW_KEYBOARD.put(51, "key.keyboard.3");
		GLFW_KEYBOARD.put(52, "key.keyboard.4");
		GLFW_KEYBOARD.put(53, "key.keyboard.5");
		GLFW_KEYBOARD.put(54, "key.keyboard.6");
		GLFW_KEYBOARD.put(55, "key.keyboard.7");
		GLFW_KEYBOARD.put(56, "key.keyboard.8");
		GLFW_KEYBOARD.put(57, "key.keyboard.9");
		GLFW_KEYBOARD.put(59, "key.keyboard.semicolon");
		GLFW_KEYBOARD.put(61, "key.keyboard.equal");
		GLFW_KEYBOARD.put(65, "key.keyboard.a");
		GLFW_KEYBOARD.put(66, "key.keyboard.b");
		GLFW_KEYBOARD.put(67, "key.keyboard.c");
		GLFW_KEYBOARD.put(68, "key.keyboard.d");
		GLFW_KEYBOARD.put(69, "key.keyboard.e");
		GLFW_KEYBOARD.put(70, "key.keyboard.f");
		GLFW_KEYBOARD.put(71, "key.keyboard.g");
		GLFW_KEYBOARD.put(72, "key.keyboard.h");
		GLFW_KEYBOARD.put(73, "key.keyboard.i");
		GLFW_KEYBOARD.put(74, "key.keyboard.j");
		GLFW_KEYBOARD.put(75, "key.keyboard.k");
		GLFW_KEYBOARD.put(76, "key.keyboard.l");
		GLFW_KEYBOARD.put(77, "key.keyboard.m");
		GLFW_KEYBOARD.put(78, "key.keyboard.n");
		GLFW_KEYBOARD.put(79, "key.keyboard.o");
		GLFW_KEYBOARD.put(80, "key.keyboard.p");
		GLFW_KEYBOARD.put(81, "key.keyboard.q");
		GLFW_KEYBOARD.put(82, "key.keyboard.r");
		GLFW_KEYBOARD.put(83, "key.keyboard.s");
		GLFW_KEYBOARD.put(84, "key.keyboard.t");
		GLFW_KEYBOARD.put(85, "key.keyboard.u");
		GLFW_KEYBOARD.put(86, "key.keyboard.v");
		GLFW_KEYBOARD.put(87, "key.keyboard.w");
		GLFW_KEYBOARD.put(88, "key.keyboard.x");
		GLFW_KEYBOARD.put(89, "key.keyboard.y");
		GLFW_KEYBOARD.put(90, "key.keyboard.z");
		GLFW_KEYBOARD.put(91, "key.keyboard.left.bracket");
		GLFW_KEYBOARD.put(92, "key.keyboard.backslash");
		GLFW_KEYBOARD.put(93, "key.keyboard.right.bracket");
		GLFW_KEYBOARD.put(96, "key.keyboard.grave.accent");
		GLFW_KEYBOARD.put(161, "key.keyboard.world.1");
		GLFW_KEYBOARD.put(162, "key.keyboard.world.2");
		GLFW_KEYBOARD.put(256, "key.keyboard.escape");
		GLFW_KEYBOARD.put(257, "key.keyboard.enter");
		GLFW_KEYBOARD.put(258, "key.keyboard.tab");
		GLFW_KEYBOARD.put(259, "key.keyboard.backspace");
		GLFW_KEYBOARD.put(260, "key.keyboard.insert");
		GLFW_KEYBOARD.put(261, "key.keyboard.delete");
		GLFW_KEYBOARD.put(262, "key.keyboard.right");
		GLFW_KEYBOARD.put(263, "key.keyboard.left");
		GLFW_KEYBOARD.put(264, "key.keyboard.down");
		GLFW_KEYBOARD.put(265, "key.keyboard.up");
		GLFW_KEYBOARD.put(266, "key.keyboard.page.up");
		GLFW_KEYBOARD.put(267, "key.keyboard.page.down");
		GLFW_KEYBOARD.put(268, "key.keyboard.home");
		GLFW_KEYBOARD.put(269, "key.keyboard.end");
		GLFW_KEYBOARD.put(280, "key.keyboard.caps.lock");
		GLFW_KEYBOARD.put(281, "key.keyboard.scroll.lock");
		GLFW_KEYBOARD.put(282, "key.keyboard.num.lock");
		GLFW_KEYBOARD.put(283, "key.keyboard.print.screen");
		GLFW_KEYBOARD.put(284, "key.keyboard.pause");
		GLFW_KEYBOARD.put(290, "key.keyboard.f1");
		GLFW_KEYBOARD.put(291, "key.keyboard.f2");
		GLFW_KEYBOARD.put(292, "key.keyboard.f3");
		GLFW_KEYBOARD.put(293, "key.keyboard.f4");
		GLFW_KEYBOARD.put(294, "key.keyboard.f5");
		GLFW_KEYBOARD.put(295, "key.keyboard.f6");
		GLFW_KEYBOARD.put(296, "key.keyboard.f7");
		GLFW_KEYBOARD.put(297, "key.keyboard.f8");
		GLFW_KEYBOARD.put(298, "key.keyboard.f9");
		GLFW_KEYBOARD.put(299, "key.keyboard.f10");
		GLFW_KEYBOARD.put(300, "key.keyboard.f11");
		GLFW_KEYBOARD.put(301, "key.keyboard.f12");
		GLFW_KEYBOARD.put(302, "key.keyboard.f13");
		GLFW_KEYBOARD.put(303, "key.keyboard.f14");
		GLFW_KEYBOARD.put(304, "key.keyboard.f15");
		GLFW_KEYBOARD.put(305, "key.keyboard.f16");
		GLFW_KEYBOARD.put(306, "key.keyboard.f17");
		GLFW_KEYBOARD.put(307, "key.keyboard.f18");
		GLFW_KEYBOARD.put(308, "key.keyboard.f19");
		GLFW_KEYBOARD.put(309, "key.keyboard.f20");
		GLFW_KEYBOARD.put(310, "key.keyboard.f21");
		GLFW_KEYBOARD.put(311, "key.keyboard.f22");
		GLFW_KEYBOARD.put(312, "key.keyboard.f23");
		GLFW_KEYBOARD.put(313, "key.keyboard.f24");

		GLFW_KEYBOARD.put(320, "key.keyboard.keypad.0");
		GLFW_KEYBOARD.put(321, "key.keyboard.keypad.1");
		GLFW_KEYBOARD.put(322, "key.keyboard.keypad.2");
		GLFW_KEYBOARD.put(323, "key.keyboard.keypad.3");
		GLFW_KEYBOARD.put(324, "key.keyboard.keypad.4");
		GLFW_KEYBOARD.put(325, "key.keyboard.keypad.5");
		GLFW_KEYBOARD.put(326, "key.keyboard.keypad.6");
		GLFW_KEYBOARD.put(327, "key.keyboard.keypad.7");
		GLFW_KEYBOARD.put(328, "key.keyboard.keypad.8");
		GLFW_KEYBOARD.put(329, "key.keyboard.keypad.9");
		GLFW_KEYBOARD.put(330, "key.keyboard.keypad.period");
		GLFW_KEYBOARD.put(331, "key.keyboard.keypad.divide");
		GLFW_KEYBOARD.put(332, "key.keyboard.keypad.multiply");
		GLFW_KEYBOARD.put(333, "key.keyboard.keypad.subtract");
		GLFW_KEYBOARD.put(334, "key.keyboard.keypad.add");
		GLFW_KEYBOARD.put(335, "key.keyboard.keypad.enter");
		GLFW_KEYBOARD.put(336, "key.keyboard.keypad.equal");
		GLFW_KEYBOARD.put(340, "key.keyboard.left.shift");
		GLFW_KEYBOARD.put(341, "key.keyboard.left.control");
		GLFW_KEYBOARD.put(342, "key.keyboard.left.alt");
		GLFW_KEYBOARD.put(343, "key.keyboard.left.win");
		GLFW_KEYBOARD.put(344, "key.keyboard.right.shift");
		GLFW_KEYBOARD.put(345, "key.keyboard.right.control");
		GLFW_KEYBOARD.put(346, "key.keyboard.right.alt");
		GLFW_KEYBOARD.put(347, "key.keyboard.right.win");
		GLFW_KEYBOARD.put(348, "key.keyboard.application");
	}
}
