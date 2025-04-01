package me.senseiwells.keybinds.api;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

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

	/**
	 * Creates a listener with a given identity.
	 *
	 * @param id The identity of the listener.
	 * @param listener The listener to wrap.
	 * @return The wrapped listener.
	 */
	static KeybindListener identity(ResourceLocation id, KeybindListener listener) {
		return new Keyed(id, listener);
	}

	@ApiStatus.Internal
	record Keyed(ResourceLocation id, KeybindListener listener) implements KeybindListener {
		@Override
		public void onPress() {
			this.listener.onPress();
		}

		@Override
		public void onRelease() {
			this.listener.onRelease();
		}

		@Override
		public void onSetKeys(InputKeys keys) {
			this.listener.onSetKeys(keys);
		}

		@Override
		public int hashCode() {
			return this.id.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			return obj instanceof Keyed other && this.id.equals(other.id);
		}
	}
}
