package me.senseiwells.keybinds.impl.mixins;

import com.mojang.blaze3d.platform.InputConstants;
import me.senseiwells.keybinds.api.InputKeys;
import me.senseiwells.keybinds.api.Keybind;
import me.senseiwells.keybinds.impl.compat.vanilla.DuckKeyBindsScreen;
import me.senseiwells.keybinds.impl.util.KeybindingUtils;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(KeyBindsScreen.class)
public class KeyBindsScreenMixin implements DuckKeyBindsScreen {
	@Shadow public long lastKeySelection;
	@Shadow private KeyBindsList keyBindsList;

	@Unique private Keybind skl$keybind;

	@Override
	public void skl$setKeybind(@Nullable Keybind keybind) {
		this.skl$keybind = keybind;
	}

	@Nullable
	@Override
	public Keybind skl$getKeybind() {
		return this.skl$keybind;
	}

	@Inject(
		method = "mouseClicked",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/screens/options/OptionsSubScreen;mouseClicked(Lnet/minecraft/client/input/MouseButtonEvent;Z)Z"
		),
		cancellable = true
	)
	private void onMouseClicked(MouseButtonEvent event, boolean bl, CallbackInfoReturnable<Boolean> cir) {
		if (this.skl$keybind != null && this.addKeyToKeybinds(InputConstants.Type.MOUSE.getOrCreate(event.button()))) {
			cir.setReturnValue(true);
		}
	}

	@Inject(
		method = "keyPressed",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/screens/options/OptionsSubScreen;keyPressed(Lnet/minecraft/client/input/KeyEvent;)Z"
		),
		cancellable = true
	)
	private void onKeyPressed(KeyEvent event, CallbackInfoReturnable<Boolean> cir) {
		if (this.skl$keybind != null) {
			if (KeybindingUtils.ESCAPE_KEYS.contains(event.key())) {
				this.skl$setKeybind(null);
				cir.setReturnValue(true);
			} else if (this.addKeyToKeybinds(InputConstants.getKey(event))) {
				cir.setReturnValue(true);
			}
			this.lastKeySelection = Util.getMillis();
			this.keyBindsList.refreshEntries();
		}
	}

	@Unique
	private boolean addKeyToKeybinds(InputConstants.Key key) {
		if (this.skl$keybind.keys().contains(key)) {
			return false;
		}
		List<InputConstants.Key> keys = new ArrayList<>(this.skl$keybind.keys().size() + 1);
		keys.addAll(this.skl$keybind.keys());
		keys.add(key);
		this.skl$keybind.setKeys(new InputKeys(keys));
		return true;
	}
}
