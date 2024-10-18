package me.senseiwells.keybinds.api;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * This interface represents a listener with events
 * that can be triggered by a keybind.
 */
public interface KeybindListener {
	/**
	 * This event is triggered when the keybind is clicked.
	 *
	 * @return Whether the listener consumed the click.
	 */
	default boolean onClick() {
		return false;
	}

	/**
	 * This event is triggered when the keybind is held.
	 */
	default void onHold() {

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
	 * the keybind is clicked.
	 *
	 * @param runnable The runnable to run.
	 * @return The created listener.
	 */
	static KeybindListener onClick(BooleanSupplier runnable) {
		return new KeybindListener() {
			@Override
			public boolean onClick() {
				return runnable.getAsBoolean();
			}
		};
	}

	/**
	 * Creates a listener that will run when
	 * the keybind is clicked.
	 *
	 * @param runnable The runnable to run.
	 * @return The created listener.
	 */
	static KeybindListener onClick(Runnable runnable) {
		return onClick(() -> {
			runnable.run();
			return true;
		});
	}

	/**
	 * Creates a listener that will run when
	 * the keybind is held.
	 *
	 * @param runnable The runnable to run.
	 * @return The created listener.
	 */
	static KeybindListener onHold(Runnable runnable) {
		return new KeybindListener() {
			@Override
			public void onHold() {
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
