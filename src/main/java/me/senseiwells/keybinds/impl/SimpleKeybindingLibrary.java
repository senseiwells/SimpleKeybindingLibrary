package me.senseiwells.keybinds.impl;

import net.minecraft.resources.ResourceLocation;

public class SimpleKeybindingLibrary {
	public static final String MOD_ID = "simple-keybinding-library";

	private SimpleKeybindingLibrary() {

	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
