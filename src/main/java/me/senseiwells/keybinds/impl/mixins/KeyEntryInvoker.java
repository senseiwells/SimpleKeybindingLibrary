package me.senseiwells.keybinds.impl.mixins;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(KeyBindsList.KeyEntry.class)
public interface KeyEntryInvoker {
	@Invoker("<init>")
	static KeyBindsList.KeyEntry construct(KeyBindsList list, KeyMapping mapping, Component component) {
		throw new AssertionError();
	}
}
