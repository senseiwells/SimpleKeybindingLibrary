package me.senseiwells.keybinds.impl.compat;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class KeybindingMixinConfig implements IMixinConfigPlugin {
    private static final String MIXIN_COMPAT = "me.senseiwells.keybinds.impl.mixins.compat.";

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.startsWith(MIXIN_COMPAT)) {
            int length = MIXIN_COMPAT.length();
            String modId = mixinClassName.substring(length, mixinClassName.indexOf('.', length));
            return FabricLoader.getInstance().isModLoaded(modId);
        }
        return true;
    }
}
