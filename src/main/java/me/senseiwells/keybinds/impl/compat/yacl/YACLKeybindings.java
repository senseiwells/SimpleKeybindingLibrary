package me.senseiwells.keybinds.impl.compat.yacl;

import dev.isxander.yacl3.config.v2.impl.autogen.OptionFactoryRegistry;
import me.senseiwells.keybinds.api.yacl.Keybinding;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class YACLKeybindings {
	private YACLKeybindings() {

	}

	static {
		OptionFactoryRegistry.registerOptionFactory(Keybinding.class, new KeybindingImpl());
	}

	public static void load() {

	}
}
