package me.senseiwells.keybinds.impl.compat.yacl;

import com.google.common.base.Suppliers;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigField;
import dev.isxander.yacl3.config.v2.api.autogen.OptionAccess;
import dev.isxander.yacl3.config.v2.api.autogen.SimpleOptionFactory;
import me.senseiwells.keybinds.api.InputKeys;
import me.senseiwells.keybinds.api.Keybind;
import me.senseiwells.keybinds.api.KeybindListener;
import me.senseiwells.keybinds.api.KeybindManager;
import me.senseiwells.keybinds.api.yacl.Keybinding;
import me.senseiwells.keybinds.api.yacl.KeybindingController;
import me.senseiwells.keybinds.impl.SimpleKeybindingLibrary;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.Optional;

@Internal
public class KeybindingImpl extends SimpleOptionFactory<Keybinding, InputKeys> {
	@Override
	public Option<InputKeys> createOption(Keybinding annotation, ConfigField<InputKeys> field, OptionAccess optionAccess) {
		Option<InputKeys> option = super.createOption(annotation, field, optionAccess);
		if (!annotation.id().isEmpty()) {
			ResourceLocation id = ResourceLocation.parse(annotation.id());
			Optional<Keybind> optional = KeybindManager.get(id);
			if (optional.isPresent()) {
				optional.get().addListener(KeybindListener.onSetKeys(keys -> {
					option.requestSet(keys);
					option.applyValue();
					field.parent().save();
				}));
			} else {
				SimpleKeybindingLibrary.logger.warn("Keybind option for {} was created before it was registered", id);
			}
			option.addListener((o, keys) -> KeybindManager.get(id).ifPresent(keybind -> keybind.setKeys(keys)));
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
