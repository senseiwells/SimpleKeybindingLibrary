package me.senseiwells.keybinds.impl.mixins.compat.controlling;

import com.blamejared.controlling.ControllingConstants;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.senseiwells.keybinds.impl.compat.vanilla.KeybindEntry;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(ControllingConstants.class)
public class ControllingConstantsMixin {
    @ModifyReturnValue(
        method = "lambda$static$0",
        at = @At("RETURN")
    )
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private static Optional<String> getSKLCategoryName(
        Optional<String> original,
        KeyBindsList.Entry entry
    ) {
        return original.or(() -> {
            if (entry instanceof KeybindEntry custom) {
                return Optional.of(custom.getCategory().label().getString());
            }
            return Optional.empty();
        });
    }

    @ModifyReturnValue(
        method = "lambda$static$1",
        at = @At("RETURN")
    )
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private static Optional<String> getSKLKeyName(
        Optional<String> original,
        KeyBindsList.Entry entry
    ) {
        return original.or(() -> {
            if (entry instanceof KeybindEntry custom) {
                return Optional.of(custom.getKeysAsString());
            }
            return Optional.empty();
        });
    }

    @ModifyReturnValue(
        method = "lambda$static$2",
        at = @At("RETURN")
    )
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private static Optional<String> getSKLKeybindName(
        Optional<String> original,
        KeyBindsList.Entry entry
    ) {
        return original.or(() -> {
            if (entry instanceof KeybindEntry custom) {
                return Optional.of(custom.getKeybindName().getString());
            }
            return Optional.empty();
        });
    }
}
