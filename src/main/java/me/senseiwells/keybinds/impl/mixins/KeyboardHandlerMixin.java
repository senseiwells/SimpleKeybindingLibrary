package me.senseiwells.keybinds.impl.mixins;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.blaze3d.platform.InputConstants;
import me.senseiwells.keybinds.api.KeybindManager;
import net.minecraft.client.KeyboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {
	@WrapWithCondition(
		method = "keyPress",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/KeyMapping;set(Lcom/mojang/blaze3d/platform/InputConstants$Key;Z)V"
		)
	)
	private boolean onKeyMappingSet(
		InputConstants.Key key,
		boolean held
	) {
		if (held) {
			KeybindManager.press(key);
		} else {
			KeybindManager.release(key);
		}
		return true;
	}
}
