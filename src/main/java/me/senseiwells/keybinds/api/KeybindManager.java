package me.senseiwells.keybinds.api;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.platform.InputConstants;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import me.senseiwells.keybinds.impl.compat.vanilla.VanillaKeybindsList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Manager for all custom keybinds.
 */
public class KeybindManager {
	private static final Logger logger = LoggerFactory.getLogger("KeybindManager");
	private static final Multimap<ResourceLocation, Consumer<Keybind>> consumers = HashMultimap.create();
	private static final Map<ResourceLocation, Keybind> keybinds = new Object2ObjectLinkedOpenHashMap<>();

	private static final List<InputConstants.Key> held = new ArrayList<>();

	private KeybindManager() {

	}

	/**
	 * Registers a custom keybind.
	 *
	 * @param id The id of the keybind.
	 * @param keybind The keybind to register.
	 * @return The registered keybind.
	 */
	public static Keybind register(ResourceLocation id, Keybind keybind) {
		Keybind previous = keybinds.put(id, keybind);
		if (previous != null) {
			logger.warn("Overwriting keybind {}, {} -> {}", id, previous.name().getString(), keybind.name().getString());
		}
		consumers.removeAll(id).forEach(c -> c.accept(keybind));
		return keybind;
	}

	/**
	 * Creates and registers a custom keybind.
	 *
	 * @param id The id of the keybind.
	 * @param keys The keys of the keybind.
	 * @return The created keybind.
	 */
	public static Keybind register(ResourceLocation id, InputKeys keys) {
		Component name = Component.translatable("key.%s.%s".formatted(id.getNamespace(), id.getPath()));
		Keybind keybind = new SimpleKeybind(name, keys);
		return register(id, keybind);
	}

	/**
	 * Creates and registers a custom keybind.
	 *
	 * @param id The id of the keybind.
	 * @return The created keybind.
	 */
	public static Keybind register(ResourceLocation id) {
		return register(id, InputKeys.EMPTY);
	}

	/**
	 * Unregisters a custom keybind
	 *
	 * @param id The id of the custom keybind to remove.
	 * @return Whether the custom keybind was removed.
	 */
	public static boolean unregister(ResourceLocation id) {
		return keybinds.remove(id) != null;
	}

	/**
	 * Adds a keybind to the vanilla controls screen.
	 * <p>
	 * Your category should be a translation key.
	 * You may use vanilla categories, such as:
	 * {@link net.minecraft.client.KeyMapping#CATEGORY_MOVEMENT}
	 *
	 * @param category The category of the keybind.
	 * @param keybind The keybind to add.
	 */
	public static void addToControlsScreen(String category, Keybind keybind) {
		VanillaKeybindsList.add(category, keybind);
	}

	/**
	 * Gets a registered keybind.
	 *
	 * @param id The id of the keybind.
	 * @return The keybind, or null if not found.
	 */
	public static Optional<Keybind> get(ResourceLocation id) {
		return Optional.ofNullable(keybinds.get(id));
	}

	@Internal
	public static void apply(ResourceLocation id, Consumer<Keybind> consumer) {
		Keybind keybind = keybinds.get(id);
		if (keybind != null) {
			consumer.accept(keybind);
			return;
		}
		consumers.put(id, consumer);
	}

	@Internal
	public static void press(InputConstants.Key key) {
		if (!held.contains(key)) {
			held.add(key);
		}
		for (Keybind keybind : keybinds.values()) {
			if (keybind.keys().isLastKey(key) && areKeysHeld(keybind.keys())) {
				keybind.hold();
				keybind.click();
			}
		}
	}

	@Internal
	public static void release(InputConstants.Key key) {
		held.remove(key);
		for (Keybind keybind : keybinds.values()) {
			if (keybind.keys().contains(key)) {
				keybind.release();
			}
		}
	}

	private static boolean areKeysHeld(InputKeys keys) {
		Iterator<InputConstants.Key> iterator = held.iterator();
		for (InputConstants.Key key : keys) {
			if (!iterator.hasNext()) {
				return false;
			}
			do {
				if (iterator.next().equals(key)) {
					break;
				}
			} while (iterator.hasNext());
		}
		return true;
	}
}
