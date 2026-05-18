package me.senseiwells.keybinds.impl.compat.vanilla;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.SortedSetMultimap;
import com.google.common.collect.TreeMultimap;
import com.mojang.datafixers.util.Either;
import me.senseiwells.keybinds.api.Keybind;
import me.senseiwells.keybinds.impl.mixins.KeyMappingCategoryAccessor;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.resources.language.I18n;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Internal
public class VanillaKeybindsList {
	private static final Multimap<KeyMapping.Category, Keybind> keybinds = HashMultimap.create();

	private VanillaKeybindsList() {

	}

	public static void add(KeyMapping.Category category, Keybind keybind) {
		keybinds.put(category, keybind);
	}

	public static void remove(Keybind keybind) {
		keybinds.values().remove(keybind);
	}

	public static SortedSetMultimap<KeyMapping.Category, Either<Keybind, KeyMapping>> merge(KeyMapping[] mappings) {
		SortedSetMultimap<KeyMapping.Category, Either<Keybind, KeyMapping>> sorted = TreeMultimap.create(
			VanillaKeybindsList::compareCategories,
			Comparator.comparing(e -> e.map(k -> k.name().getString(), k -> I18n.get(k.getName())))
		);
		for (KeyMapping mapping : mappings) {
			sorted.put(mapping.getCategory(), Either.right(mapping));
		}
		for (Map.Entry<KeyMapping.Category, Keybind> entry : keybinds.entries()) {
			sorted.put(entry.getKey(), Either.left(entry.getValue()));
		}
		return sorted;
	}

	private static int compareCategories(KeyMapping.Category a, KeyMapping.Category b) {
		List<KeyMapping.Category> order = KeyMappingCategoryAccessor.getSortOrder();
		int orderA = order.indexOf(a);
        int orderB = order.indexOf(b);
		return Integer.compare(orderA, orderB);
	}
}
