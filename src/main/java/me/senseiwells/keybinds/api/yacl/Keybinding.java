package me.senseiwells.keybinds.api.yacl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A regular option factory.
 * <p>
 * This creates a regular option with a
 * {@link KeybindingController} controller.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Keybinding {
	/**
	 * The id of the keybind to hook onto.
	 *
	 * @return The id of the keybind.
	 */
	String id() default "";
}
