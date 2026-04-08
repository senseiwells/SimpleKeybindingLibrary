package me.senseiwells.keybinds.impl.compat.vanilla;

import com.google.common.collect.ImmutableList;
import me.senseiwells.keybinds.api.InputKeys;
import me.senseiwells.keybinds.api.Keybind;
import me.senseiwells.keybinds.impl.mixins.KeyBindsListAccessor;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.List;
import java.util.stream.Collectors;

@Internal
public class KeybindEntry extends KeyBindsList.Entry {
	private final KeyBindsList list;
	private final Keybind keybind;

	private final Button changeButton;
	private final Button resetButton;

	private final KeyMapping.Category category;

	public KeybindEntry(KeyBindsList list, Keybind keybind, KeyMapping.Category category) {
		this.list = list;
		this.keybind = keybind;
		this.category = category;

		Component name = keybind.name();
		this.changeButton = Button.builder(name, _ -> {
			KeyBindsScreen screen = ((KeyBindsListAccessor) this.list).getKeyBindsScreen();
			screen.selectedKey = null;
			((DuckKeyBindsScreen) screen).skl$setKeybind(this.keybind);
			this.keybind.clearKeys();
			this.list.resetMappingAndUpdateButtons();
		}).bounds(0, 0, 75, 20).createNarration(supplier -> {
			return this.keybind.keys().isEmpty() ? Component.translatable("narrator.controls.unbound", name)
				: Component.translatable("narrator.controls.bound", name, supplier.get());
		}).build();
		this.resetButton = Button.builder(Component.translatable("controls.reset"), _ -> {
			this.keybind.resetKeysToDefault();
			this.list.resetMappingAndUpdateButtons();
		}).bounds(0, 0, 50, 20).createNarration(_ -> {
			return Component.translatable("narrator.controls.reset", name);
		}).build();

		this.refreshEntry();
	}

	public KeyMapping.Category getCategory() {
		return this.category;
	}

	public InputKeys getKeys() {
		return this.keybind.keys();
	}

	public String getKeysAsString() {
		return this.keybind.keys().stream()
			.map(key -> key.getDisplayName().getString())
			.collect(Collectors.joining(" "));
	}

	public Component getKeybindName() {
		return this.keybind.name();
	}

	@Override
	public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
		int scrollbarPosition = this.list.getRowRight() + 8;
		int resetButtonX = scrollbarPosition - this.resetButton.getWidth() - 10;
		int buttonY = this.getContentY() - 2;
		this.resetButton.setPosition(resetButtonX, buttonY);
		this.resetButton.extractRenderState(graphics, mouseX, mouseY, a);
		int changeButtonX = resetButtonX - 5 - this.changeButton.getWidth();
		this.changeButton.setPosition(changeButtonX, buttonY);
		this.changeButton.extractRenderState(graphics, mouseX, mouseY, a);
		Font font = Minecraft.getInstance().font;
		graphics.text(font, this.keybind.name(), this.getContentX(), this.getContentYMiddle() - 9 / 2, -1);
	}

	@Override
	public List<? extends NarratableEntry> narratables() {
		return ImmutableList.of(this.changeButton, this.resetButton);
	}

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
