package me.senseiwells.keybinds.impl.mixins;

import net.minecraft.client.gui.components.AbstractSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractSelectionList.class)
public interface AbstractSelectionListAccessor {
	@Invoker("getScrollbarPosition")
	int getCurrentScrollbarPosition();
}
