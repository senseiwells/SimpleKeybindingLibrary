# Simple Keybinding Library

A simple and lightweight library for creating and managing keybinds in Minecraft.

Adds support to create custom multi-key keybinds which can be easily integrated
into the vanilla controls menu, or into [YACL](https://github.com/isXander/YetAnotherConfigLib).
YACL is entirely optional, this library has no dependencies.

## Adding to dependencies

If you are developing minigames using arcade, you will want to include
all the modules, you can do this by adding the following to your
`build.gradle.kts`.

It is recommended you JiJ to include the library as it has a small footprint.

```kts
repositories {
    maven("https://maven.supersanta.me/snapshots")
}

dependencies {
    include(modImplementation("me.senseiwells:simple-keybinding-library:0.1.13+1.21.5")!!)
}
```

## Usage

You can create a keybind through the `KeybindManager` by providing it a `ResourceLocation`
and a set input keys which will be the default keys for the keybind.

You can add the keybind to the vanilla controls screen by calling `KeybindManager#addToControlsScreen`
and passing a category and your keybind.

You can add listeners to your keybind, alternatively you can poll your keybind every
tick and retrieve the number of times it was clicked within the tick.

Serializing the `InputKeys` for your keybind can be done with `Gson`.

See below for an example:

```java
public class ExampleMod implements ModInitializer {
    public static final Keybind exampleKeybind = KeybindManager.register(
        ResourceLocation.parse("modid:my_keybind"),
        InputKeys.of(InputConstants.KEY_LSHIFT, InputConstants.KEY_F6)
    );

    @Override
    public void onInitialize() {
        // Add to Minecraft's control screen for configuration
        KeybindManager.addToControlsScreen(KeyMapping.CATEGORY_GAMEPLAY, exampleKeybind);
        
        exampleKeybind.addListener(KeybindListener.onPress(() -> {
            System.out.println("Example keybind was pressed!");
        }));
        exampleKeybind.addListener(KeybindListener.onRelease(() -> {
            System.out.println("Example keybind was released!");
        }));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (exampleKeybind.consumeClicks() > 0) {
                System.out.println("Polled example keybind!");
            }
        });
        
        // Serialize and Deserialize
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(InputKeys.class, InputKeys.Serializer.INSTANCE)
            .create();
        
        JsonElement json = gson.toJsonTree(exampleKeybind.keys());
        InputKeys keys = gson.fromJson(json, InputKeys.class);
        
        // Load keys into keybind
        exampleKeybind.setKeys(keys);
    }
}
```

### YACL

If you are using YACL for your config library, you can use the provided annotations
to easily interface with it.

```java
public class ExampleConfig {
    @SerialEntry
    @AutoGen(category = "keybinds")
    // This will automatically bind to a keybind with the specified id
    @Keybinding(id = ExampleMod.EXAMPLE_KEYBIND_ID)
    public InputKeys exampleKeybindKeys = InputKeys.of(InputConstants.KEY_F6);
}

// ...

public class ExampleMod implements ModInitializer { 
    // YACL config handler
    public static final ConfigClassHandler<ExampleConfig> HANDLER = ConfigClassHandler.createBuilder(ExampleConfig.class)
        .id(ResourceLocation.fromNamespaceAndPath("modid", "config"))
        .serializer(config -> {
            return GsonConfigSerializerBuilder.create(config)
                .setPath(FabricLoader.getInstance().getConfigDir().resolve("example-config.json"))
                .appendGsonBuilder(gson -> {
                    return gson.setPrettyPrinting()
                        // We must add a type adapter for InputKeys
                        .registerTypeAdapter(InputKeys.class, InputKeys.Serializer.INSTANCE);
                })
                .build();
        })
        .build();

    public static final String EXAMPLE_KEYBIND_ID = "modid:example_keybind";
    public static final Keybind exampleKeybind;

    static {
        HANDLER.load();

        exampleKeybind = KeybindManager.register(
            ResourceLocation.parse(EXAMPLE_KEYBIND_ID),
            // We load the input keys for the keybind
            HANDLER.instance().exampleKeybindKeys
        );
    }
    
    @Override
    public void onInitialize() { }
}
```