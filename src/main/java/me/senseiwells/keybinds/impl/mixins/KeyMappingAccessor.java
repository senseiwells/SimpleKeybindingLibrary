package me.senseiwells.keybinds.impl.mixins;

import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(KeyMapping.class)
public interface KeyMappingAccessor {
	@Accessor("CATEGORY_SORT_ORDER")
	static Map<String, Integer> getCategorySortOrder() {
		throw new AssertionError();
	}
}
