package me.senseiwells.keybinds.impl;

import net.minecraft.resources.Identifier;

public class SimpleKeybindingLibrary {
	public static final String MOD_ID = "simple-keybinding-library";

	private SimpleKeybindingLibrary() {

	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
