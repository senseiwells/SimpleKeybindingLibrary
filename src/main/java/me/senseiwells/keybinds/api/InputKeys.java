package me.senseiwells.keybinds.api;

import com.google.gson.*;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.IntStream;

/**
 * This class represents a list of ordered input keys.
 */
public class InputKeys extends AbstractCollection<InputConstants.Key> {
	private static final Component SEPARATOR = Component.literal(" + ");
	public static final InputKeys EMPTY = new InputKeys(List.of());

	private final List<InputConstants.Key> keys;

	public InputKeys(List<InputConstants.Key> keys) {
		this.keys = keys.stream().distinct().toList();
	}

	public InputKeys(InputConstants.Key[] keys) {
		this.keys = Arrays.stream(keys).distinct().toList();
	}

	public boolean isLastKey(InputConstants.Key key) {
		return !this.keys.isEmpty() && this.keys.getLast().equals(key);
	}

	@NotNull
	@Override
	public Iterator<InputConstants.Key> iterator() {
		return this.keys.iterator();
	}

	@Override
	public int size() {
		return this.keys.size();
	}

	@Override
	public boolean contains(Object o) {
		return this.keys.contains(o);
	}

	/**
	 * This formats the specified keys using their
	 * translated names separated by <code>" + "</code>.
	 *
	 * @param keys The keys to format.
	 * @return The formatted keys.
	 */
	public static MutableComponent format(Collection<InputConstants.Key> keys) {
		if (keys.isEmpty()) {
			return Component.empty().append(InputConstants.UNKNOWN.getDisplayName());
		}
		return ComponentUtils.formatList(keys, SEPARATOR, InputConstants.Key::getDisplayName);
	}

	/**
	 * This formats the specified keys using their
	 * translated names separated by <code>" + "</code>,
	 * additionally with a <code>"> "</code> and <code>" <"</code>
	 * appended as a prefix and suffix respectively.
	 *
	 * @param keys The keys to format.
	 * @return The formatted keys.
	 */
	public static Component formatEditing(Collection<InputConstants.Key> keys) {
		return Component.literal("> ")
			.append(format(keys).withStyle(ChatFormatting.WHITE, ChatFormatting.UNDERLINE))
			.append(" <")
			.withStyle(ChatFormatting.YELLOW);
	}

	/**
	 * Creates an instance of {@link InputKeys} with
	 * keyboard keycodes.
	 *
	 * @param keys The specified keys.
	 * @return A {@link InputKeys} instance.
	 */
	public static InputKeys of(int ...keys) {
		return new InputKeys(IntStream.of(keys).mapToObj(InputConstants.Type.KEYSYM::getOrCreate).toList());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o instanceof InputKeys) {
			return Objects.equals(this.keys, ((InputKeys) o).keys);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.keys);
	}

	public static class Serializer implements JsonSerializer<InputKeys>, JsonDeserializer<InputKeys> {
		public static final Serializer INSTANCE = new Serializer();

		private Serializer() {

		}

		@Override
		public InputKeys deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			List<InputConstants.Key> keys = new ArrayList<>();
			JsonArray array = (JsonArray) json;
			for (JsonElement element : array) {
				JsonObject object = (JsonObject) element;
				InputConstants.Type type = context.deserialize(object.get("type"), InputConstants.Type.class);
				int code = object.get("code").getAsInt();
				keys.add(type.getOrCreate(code));
			}
			return new InputKeys(keys);
		}

		@Override
		public JsonElement serialize(InputKeys src, Type typeOfSrc, JsonSerializationContext context) {
			JsonArray array = new JsonArray();
			for (InputConstants.Key key : src) {
				JsonObject object = new JsonObject();
				object.add("type", context.serialize(key.getType()));
				object.addProperty("code", key.getValue());
				array.add(object);
			}
			return array;
		}
	}
}
