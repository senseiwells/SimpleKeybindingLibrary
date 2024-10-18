package me.senseiwells.keybinds.impl.mixins;

import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Either;
import me.senseiwells.keybinds.api.Keybind;
import me.senseiwells.keybinds.impl.compat.vanilla.KeybindEntry;
import me.senseiwells.keybinds.impl.compat.vanilla.VanillaKeybindsList;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.Map;

@Mixin(KeyBindsList.class)
public class KeyBindsListMixin extends ContainerObjectSelectionList<KeyBindsList.Entry> {
	@Shadow private int maxNameWidth;

	public KeyBindsListMixin(Minecraft minecraft, int width, int height, int y, int itemHeight) {
		super(minecraft, width, height, y, itemHeight);
	}

	@Inject(
		method = "<init>",
		at = @At("TAIL")
	)
	private void beforeAddingEntries(
		KeyBindsScreen keyBindsScreen,
		Minecraft minecraft,
		CallbackInfo ci
	) {
		this.clearEntries();

		Multimap<String, Either<Keybind, KeyMapping>> sorted = VanillaKeybindsList.merge(minecraft.options.keyMappings);

		KeyBindsList self = (KeyBindsList) (Object) this;
		for (Map.Entry<String, Collection<Either<Keybind, KeyMapping>>> entry : sorted.asMap().entrySet()) {
			this.addEntry(self.new CategoryEntry(Component.translatable(entry.getKey())));
			for (Either<Keybind, KeyMapping> either : entry.getValue()) {
				either.ifLeft(keybind -> {
					this.addEntry(new KeybindEntry(self, keybind));
					this.maxNameWidth = Math.max(this.maxNameWidth, minecraft.font.width(keybind.name()));
				});
				either.ifRight(mapping -> {
					Component name = Component.translatable(mapping.getName());
					this.addEntry(KeyEntryInvoker.construct(self, mapping, name));
					this.maxNameWidth = Math.max(this.maxNameWidth, minecraft.font.width(name));
				});
			}
		}
	}
}
