package me.senseiwells.keybinds.impl.mixins.compat.controlling;

import com.blamejared.controlling.api.SortOrder;
import com.blamejared.controlling.api.entries.IKeyEntry;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.senseiwells.keybinds.impl.compat.vanilla.KeybindEntry;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Comparator;

@Mixin(SortOrder.class)
public class SortOrderMixin {
    @Definition(id = "sorter", field = "Lcom/blamejared/controlling/api/SortOrder;sorter:Ljava/util/Comparator;")
    @Expression("this.sorter = @(?)")
    @ModifyExpressionValue(
        method = "<init>",
        at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private Comparator<KeyBindsList.Entry> replaceCompareMethod(Comparator<KeyBindsList.Entry> original) {
        SortOrder self = (SortOrder) (Object) this;

        Comparator<KeyBindsList.Entry> comparator;
        if (self.ordinal() == 1) {
            comparator = Comparator.comparing(this::getKeybindName);
        } else if (self.ordinal() == 2) {
            comparator = Comparator.comparing(this::getKeybindName, Comparator.reverseOrder());
        } else if (self.ordinal() == 3) {
            comparator = Comparator.comparing(this::getKeybindKeys)
                .thenComparing(this::getKeybindName);
        } else if (self.ordinal() == 4) {
            comparator = Comparator.comparing(this::getKeybindKeys, Comparator.reverseOrder())
                .thenComparing(this::getKeybindName, Comparator.reverseOrder());
        } else {
            return Comparator.comparing(_ -> 0);
        }
        return comparator.thenComparing(original);
    }

    @Unique
    private String getKeybindName(KeyBindsList.Entry entry) {
        return switch (entry) {
            case IKeyEntry impl -> impl.getName().getString();
            case KeybindEntry impl -> impl.getKeybindName().getString();
            default -> throw new IllegalStateException("Cannot sort entry");
        };
    }

    @Unique
    private String getKeybindKeys(KeyBindsList.Entry entry) {
        return switch (entry) {
            case IKeyEntry impl -> impl.getKey().getTranslatedKeyMessage().getString();
            case KeybindEntry impl -> impl.getKeysAsString();
            default -> throw new IllegalStateException("Cannot sort entry");
        };
    }
}
