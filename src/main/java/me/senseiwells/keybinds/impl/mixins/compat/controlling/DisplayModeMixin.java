package me.senseiwells.keybinds.impl.mixins.compat.controlling;

import com.blamejared.controlling.api.DisplayMode;
import me.senseiwells.keybinds.impl.compat.vanilla.KeybindEntry;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DisplayMode.class)
public class DisplayModeMixin {
    @Inject(
        method = "lambda$getPredicate$0",
        at = @At("HEAD"),
        cancellable = true
    )
    private void dontIgnoreSKLEntries(KeyBindsList.Entry entry, CallbackInfoReturnable<Boolean> cir) {
        if (entry instanceof KeybindEntry) {
            cir.setReturnValue(true);
        }
    }
}
