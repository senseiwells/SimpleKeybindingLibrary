package me.senseiwells.keybinds.impl.mixins;

import me.senseiwells.keybinds.impl.compat.vanilla.DuckKeyBindsScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyBindsList.KeyEntry.class)
public class KeyEntryMixin {
	@Inject(
		method = "lambda$new$0",
		at = @At("HEAD")
	)
	private static void resetCustomKeybindWhenOnVanillaChange(
		KeyBindsList list,
		KeyMapping key,
		Button button,
		CallbackInfo ci
	) {
		KeyBindsScreen screen = ((KeyBindsListAccessor) list).getKeyBindsScreen();
		((DuckKeyBindsScreen) screen).skl$setKeybind(null);
	}
}
