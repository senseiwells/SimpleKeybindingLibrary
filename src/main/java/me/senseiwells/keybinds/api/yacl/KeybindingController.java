package me.senseiwells.keybinds.api.yacl;

import com.mojang.blaze3d.platform.InputConstants;
import dev.isxander.yacl3.api.Controller;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.AbstractWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.ControllerWidget;
import it.unimi.dsi.fastutil.ints.IntList;
import me.senseiwells.keybinds.api.InputKeys;
import me.senseiwells.keybinds.impl.util.KeybindingUtils;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * A keybind controller that allows you to input
 * key/mouse presses to set a {@link me.senseiwells.keybinds.api.Keybind}.
 *
 * @param option The bound option.
 */
public record KeybindingController(
	Option<InputKeys> option
) implements Controller<InputKeys> {
	@Override
	public Option<InputKeys> option() {
		return this.option;
	}

	@Override
	public Component formatValue() {
		return InputKeys.format(this.option.pendingValue());
	}

	@Override
	public AbstractWidget provideWidget(YACLScreen screen, Dimension<Integer> widgetDimension) {
		return new KeybindingControllerElement(this, screen, widgetDimension);
	}

	public static class KeybindingControllerElement extends ControllerWidget<KeybindingController> {
		private final List<InputConstants.Key> keys = new ArrayList<>();

		public KeybindingControllerElement(KeybindingController control, YACLScreen screen, Dimension<Integer> dim) {
			super(control, screen, dim);
		}

		@Override
		protected int getHoveredControlWidth() {
			return this.getUnhoveredControlWidth();
		}

		@Override
		protected Component getValueText() {
			if (this.isFocused()) {
				return InputKeys.formatEditing(this.keys);
			}
			return super.getValueText();
		}

		@Override
		public void unfocus() {
			super.unfocus();
			this.keys.clear();
		}

		@Override
		public boolean mouseClicked(double mouseX, double mouseY, int button) {
			if (this.isAvailable() && this.getDimension().isPointInside((int) mouseX, (int) mouseY)) {
				if (!this.isFocused()) {
					this.setFocused(true);
					return true;
				}

				InputConstants.Key key = InputConstants.Type.MOUSE.getOrCreate(button);
				if (!this.keys.contains(key)) {
					this.keys.add(key);
				}
				return true;
			}
			this.unfocus();
			return false;
		}

		@Override
		public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
			if (!this.isFocused()) {
				return false;
			}

			if (KeybindingUtils.ESCAPE_KEYS.contains(keyCode)) {
				this.control.option().requestSet(new InputKeys(this.keys));
				this.unfocus();
				return true;
			}
			InputConstants.Key key = InputConstants.getKey(keyCode, scanCode);
			if (!this.keys.contains(key)) {
				this.keys.add(key);
			}
			return true;
		}
	}
}
