package me.senseiwells.keybinds.impl.mixins.compat.controlling;

import com.blamejared.controlling.api.entries.IKeyEntry;
import com.blamejared.controlling.client.NewKeyBindsScreen;
import me.senseiwells.keybinds.impl.compat.vanilla.KeybindEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.Predicate;

@Mixin(NewKeyBindsScreen.class)
public class NewKeybindsScreenMixin {
    @Redirect(
        method = "lambda$filterKeys$2",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/List;removeIf(Ljava/util/function/Predicate;)Z"
        )
    )
    private static boolean removeNonKeybindEntries(List<Object> instance, Predicate<?> predicate) {
        return instance.removeIf(o -> !(o instanceof IKeyEntry || o instanceof KeybindEntry));
    }
}
