package me.senseiwells.keybinds.impl.compat.vanilla;

import com.google.common.collect.ImmutableList;
import me.senseiwells.keybinds.api.InputKeys;
import me.senseiwells.keybinds.api.Keybind;
import me.senseiwells.keybinds.impl.mixins.KeyBindsListAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Internal
public class KeybindEntry extends KeyBindsList.Entry {
	private final KeyBindsList list;
	private final Keybind keybind;

	private final Button changeButton;
	private final Button resetButton;

	public KeybindEntry(KeyBindsList list, Keybind keybind) {
		this.list = list;
		this.keybind = keybind;

		Component name = keybind.name();
		this.changeButton = Button.builder(name, button -> {
			KeyBindsScreen screen = ((KeyBindsListAccessor) this.list).getKeyBindsScreen();
			screen.selectedKey = null;
			((DuckKeyBindsScreen) screen).skl$setKeybind(this.keybind);
			this.keybind.clearKeys();
			this.list.resetMappingAndUpdateButtons();
		}).bounds(0, 0, 75, 20).createNarration(supplier -> {
			return this.keybind.keys().isEmpty() ? Component.translatable("narrator.controls.unbound", name)
				: Component.translatable("narrator.controls.bound", name, supplier.get());
		}).build();
		this.resetButton = Button.builder(Component.translatable("controls.reset"), button -> {
			this.keybind.resetKeysToDefault();
			this.list.resetMappingAndUpdateButtons();
		}).bounds(0, 0, 50, 20).createNarration((supplier) -> {
			return Component.translatable("narrator.controls.reset", name);
		}).build();

		this.refreshEntry();
	}

	@Override
	public void render(
		GuiGraphics graphics,
		int index,
		int top,
		int left,
		int width,
		int height,
		int mouseX,
		int mouseY,
		boolean hovering,
		float partialTick
	) {
		int scrollbarPosition = this.list.getRowRight() + 8;
		int resetButtonX = scrollbarPosition - this.resetButton.getWidth() - 10;
		int buttonY = top - 2;
		this.resetButton.setPosition(resetButtonX, buttonY);
		this.resetButton.render(graphics, mouseX, mouseY, partialTick);
		int changeButtonX = resetButtonX - 5 - this.changeButton.getWidth();
		this.changeButton.setPosition(changeButtonX, buttonY);
		this.changeButton.render(graphics, mouseX, mouseY, partialTick);
		Font font = Minecraft.getInstance().font;
		int middle = top + height / 2;
		graphics.drawString(font, this.keybind.name(), left, middle - 9 / 2, -1);
	}

	@NotNull
	@Override
	public List<? extends NarratableEntry> narratables() {
		return ImmutableList.of(this.changeButton, this.resetButton);
	}

	@NotNull
	@Override
	public List<? extends GuiEventListener> children() {
		return ImmutableList.of(this.changeButton, this.resetButton);
	}

	@Override
	public void refreshEntry() {
		this.resetButton.active = !this.keybind.areKeysDefault();
		KeyBindsScreen screen = ((KeyBindsListAccessor) this.list).getKeyBindsScreen();
		if (((DuckKeyBindsScreen) screen).skl$getKeybind() == this.keybind) {
			this.changeButton.setMessage(InputKeys.formatEditing(this.keybind.keys()));
		} else {
			this.changeButton.setMessage(InputKeys.format(this.keybind.keys()));
		}
	}
}
