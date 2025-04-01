package me.senseiwells.keybinds.impl.mixins.compat.yacl;

import dev.isxander.yacl3.gui.YACLScreen;
import me.senseiwells.keybinds.impl.compat.yacl.EscapeCloseable;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.components.tabs.TabManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(YACLScreen.class)
public class YACLScreenMixin {
    @Shadow @Final public TabManager tabManager;

    @Inject(
        method = "shouldCloseOnEsc",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onShouldCloseOnEsc(CallbackInfoReturnable<Boolean> cir) {
        Tab tab = this.tabManager.getCurrentTab();
        if (tab != null) {
            boolean[] close = {true};
            tab.visitChildren(widget -> {
                if (close[0] && widget instanceof ContainerEventHandler container) {
                    if (this.recursivelyCheckNotClosable(container)) {
                        close[0] = false;
                    }
                }
            });
            if (!close[0]) {
                cir.setReturnValue(false);
            }
        }
    }

    @Unique
    private boolean recursivelyCheckNotClosable(ContainerEventHandler container) {
        for (GuiEventListener child : container.children()) {
            if (child instanceof EscapeCloseable closeable && !closeable.shouldCloseOnEsc()) {
                return true;
            }
            if (child instanceof ContainerEventHandler inner && this.recursivelyCheckNotClosable(inner)) {
                return true;
            }
        }
        return false;
    }
}
