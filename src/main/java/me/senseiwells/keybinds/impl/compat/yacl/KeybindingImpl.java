package me.senseiwells.keybinds.impl.compat.yacl;

import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionFlag;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigField;
import dev.isxander.yacl3.config.v2.api.autogen.OptionAccess;
import dev.isxander.yacl3.config.v2.api.autogen.SimpleOptionFactory;
import me.senseiwells.keybinds.api.InputKeys;
import me.senseiwells.keybinds.api.KeybindListener;
import me.senseiwells.keybinds.api.KeybindManager;
import me.senseiwells.keybinds.api.yacl.Keybinding;
import me.senseiwells.keybinds.api.yacl.KeybindingController;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class KeybindingImpl extends SimpleOptionFactory<Keybinding, InputKeys> {
	@Override
	public Option<InputKeys> createOption(
		Keybinding annotation,
		ConfigField<InputKeys> field,
		OptionAccess optionAccess
	) {
		Option<InputKeys> option = super.createOption(annotation, field, optionAccess);
		if (!annotation.id().isEmpty()) {
			ResourceLocation id = ResourceLocation.parse(annotation.id());
			KeybindManager.apply(id, keybind -> {
				keybind.addListener(KeybindListener.onSetKeys(keys -> {
					option.requestSet(keys);
					option.applyValue();
					field.parent().save();
				}));
				option.addEventListener((o, e) -> keybind.setKeys(o.pendingValue()));
			});
		}
		return option;
	}

	@Override
	protected ControllerBuilder<InputKeys> createController(
		Keybinding annotation,
		ConfigField<InputKeys> field,
		OptionAccess storage,
		Option<InputKeys> option
	) {
		return () -> new KeybindingController(option);
	}
}
