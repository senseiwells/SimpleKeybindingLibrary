package me.senseiwells.keybinds.impl.compat.vanilla;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.SortedSetMultimap;
import com.google.common.collect.TreeMultimap;
import com.mojang.datafixers.util.Either;
import me.senseiwells.keybinds.api.Keybind;
import me.senseiwells.keybinds.impl.mixins.KeyMappingAccessor;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.resources.language.I18n;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.Comparator;
import java.util.Map;

@Internal
public class VanillaKeybindsList {
	private static final Multimap<String, Keybind> keybinds = HashMultimap.create();

	private VanillaKeybindsList() {

	}

	public static void add(String category, Keybind keybind) {
		keybinds.put(category, keybind);
	}

	public static SortedSetMultimap<String, Either<Keybind, KeyMapping>> merge(KeyMapping[] mappings) {
		SortedSetMultimap<String, Either<Keybind, KeyMapping>> sorted = TreeMultimap.create(
			VanillaKeybindsList::compareCategories,
			Comparator.comparing(e -> e.map(k -> k.name().getString(), k -> I18n.get(k.getName())))
		);
		for (KeyMapping mapping : mappings) {
			sorted.put(mapping.getCategory(), Either.right(mapping));
		}
		for (Map.Entry<String, Keybind> entry : keybinds.entries()) {
			sorted.put(entry.getKey(), Either.left(entry.getValue()));
		}
		return sorted;
	}

	private static int compareCategories(String a, String b) {
		Map<String, Integer> order = KeyMappingAccessor.getCategorySortOrder();
		Integer orderA = order.get(a);
		Integer orderB = order.get(b);
		if (orderA == null) {
			if (orderB == null) {
				return a.compareTo(b);
			}
			return orderB;
		} else if (orderB == null) {
			return orderA;
		}
		return orderA.compareTo(orderB);
	}
}
