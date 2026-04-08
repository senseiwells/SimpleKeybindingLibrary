package me.senseiwells.keybinds.impl.mixins.compat.controlling;

import com.blamejared.controlling.client.CustomList;
import com.blamejared.controlling.client.NewKeyBindsList;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Either;
import me.senseiwells.keybinds.api.Keybind;
import me.senseiwells.keybinds.impl.compat.vanilla.KeybindEntry;
import me.senseiwells.keybinds.impl.compat.vanilla.VanillaKeybindsList;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.Map;

@Mixin(NewKeyBindsList.class)
public abstract class NewKeyBindsListMixin extends CustomList {
	@Shadow
	private int maxListLabelWidth;

	@Shadow
	protected abstract boolean shouldShow(Component component);

	public NewKeyBindsListMixin(KeyBindsScreen screen, Minecraft minecraft) {
		super(screen, minecraft);
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

		Multimap<KeyMapping.Category, Either<Keybind, KeyMapping>> sorted = VanillaKeybindsList.merge(minecraft.options.keyMappings);

		NewKeyBindsList self = (NewKeyBindsList) (Object) this;
		for (Map.Entry<KeyMapping.Category, Collection<Either<Keybind, KeyMapping>>> entry : sorted.asMap().entrySet()) {
			if (this.shouldShow(entry.getKey().label())) {
				this.addEntry(self.new CategoryEntry(entry.getKey()));
				for (Either<Keybind, KeyMapping> either : entry.getValue()) {
					either.ifLeft(keybind -> {
						this.addEntry(new KeybindEntry(self, keybind, entry.getKey()));
						this.maxListLabelWidth = Math.max(this.maxListLabelWidth, minecraft.font.width(keybind.name()));
					});
					either.ifRight(mapping -> {
						Component name = Component.translatable(mapping.getName());
						this.addEntry(self.new KeyEntry(mapping, name));
						this.maxListLabelWidth = Math.max(this.maxListLabelWidth, minecraft.font.width(name));
					});
				}
			}
		}
	}
}
