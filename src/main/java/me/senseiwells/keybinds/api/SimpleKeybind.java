package me.senseiwells.keybinds.api;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.*;
import java.util.function.Consumer;

/**
 * Simple implementation of the {@link Keybind} interface.
 */
public class SimpleKeybind implements Keybind {
	private final Set<KeybindListener> listeners = new LinkedHashSet<>();

	private final Component name;
	private final InputKeys defaults;
	private InputKeys keys;

	private boolean held = false;
	private int clicks = 0;

	public SimpleKeybind(Component name) {
		this(name, InputKeys.EMPTY);
	}

	public SimpleKeybind(Component name, InputKeys keys) {
		this.name = name;
		this.keys = keys;
		this.defaults = keys;
	}

	@Override
	public Component name() {
		return this.name;
	}

	@Override
	public InputKeys keys() {
		return this.keys;
	}

	@Override
	public void hold() {
		if (!this.held) {
			this.held = true;
			this.forEachListener(KeybindListener::onPress);
		}
	}

	@Override
	public void click() {
		this.clicks++;
	}

	@Override
	public void release() {
		if (this.held) {
			this.held = false;
			this.forEachListener(KeybindListener::onRelease);
		}
	}

	@Override
	public boolean isHeld() {
		return this.held;
	}

	@Override
	public int consumeClicks() {
		int clicks = this.clicks;
		this.clicks = 0;
		return clicks;
	}

	@Override
	public boolean areKeysDefault() {
		return this.defaults.equals(this.keys);
	}

	@Override
	public void addListener(KeybindListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public void setKeys(InputKeys keys) {
		if (!this.keys.equals(keys)) {
			this.keys = keys;
			this.forEachListener(listener -> listener.onSetKeys(keys));
		}
	}

	@Override
	public void resetKeysToDefault() {
		this.setKeys(this.defaults);
	}

	private void forEachListener(Consumer<KeybindListener> consumer) {
		List<KeybindListener> copy = new ArrayList<>(this.listeners);
		for (KeybindListener listener : copy) {
			consumer.accept(listener);
		}
	}
}
