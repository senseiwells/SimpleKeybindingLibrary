package me.senseiwells.keybinds.api;

import java.util.function.Consumer;

/**
 * This interface represents a listener with events
 * that can be triggered by a keybind.
 */
public interface KeybindListener {
	/**
	 * This event is triggered when the keybind is pressed.
	 */
	default void onPress() {

	}

	/**
	 * This event is triggered when the keybind is released.
	 */
	default void onRelease() {

	}

	/**
	 * This event is triggered when the keybind is set.
	 *
	 * @param keys The keys that were set.
	 */
	default void onSetKeys(InputKeys keys) {

	}


	/**
	 * Creates a listener that will run when
	 * the keybind is held.
	 *
	 * @param runnable The runnable to run.
	 * @return The created listener.
	 */
	static KeybindListener onPress(Runnable runnable) {
		return new KeybindListener() {
			@Override
			public void onPress() {
				runnable.run();
			}
		};
	}

	/**
	 * Creates a listener that will run when
	 * the keybind is released.
	 *
	 * @param runnable The runnable to run.
	 * @return The created listener.
	 */
	static KeybindListener onRelease(Runnable runnable) {
		return new KeybindListener() {
			@Override
			public void onRelease() {
				runnable.run();
			}
		};
	}

	/**
	 * Creates a listener that will run when
	 * the keybind is set.
	 *
	 * @param consumer The consumer to run.
	 * @return The created listener.
	 */
	static KeybindListener onSetKeys(Consumer<InputKeys> consumer) {
		return new KeybindListener() {
			@Override
			public void onSetKeys(InputKeys keys) {
				consumer.accept(keys);
			}
		};
	}
}
