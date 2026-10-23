package indi.ohtoai.tool.no_hide_flags;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Single entrypoint of the fat jar. Picks the variant compiled for the
 * running Minecraft era — legacy121 (1.21-1.21.4), legacy125 (1.21.5-1.21.8)
 * or modern26 (26.x) — and loads it reflectively, so the dormant variants
 * are never linked on the wrong runtime.
 */
public final class NoHideFlagsDispatcher implements ClientModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("no-hide-flags");
    private static final Pattern VERSION_PATTERN = Pattern.compile("^(\\d+)\\.(\\d+)(?:\\.(\\d+))?");

    @Override
    public void onInitializeClient() {
        String variant = chooseVariant(currentVersion());
        if (variant == null) {
            LOGGER.warn("Unsupported Minecraft version, No Hide Flags stays inactive");
            return;
        }
        String className = "indi.ohtoai.tool.no_hide_flags." + variant + ".NoHideFlagsClient";
        try {
            ClientModInitializer impl = (ClientModInitializer) Class.forName(
                className, true, NoHideFlagsDispatcher.class.getClassLoader())
                .getDeclaredConstructor()
                .newInstance();
            impl.onInitializeClient();
            LOGGER.info("No Hide Flags active ({})", variant);
        } catch (Throwable t) {
            LOGGER.error("Failed to start No Hide Flags variant {}", variant, t);
        }
    }

    private static String currentVersion() {
        try {
            return FabricLoader.getInstance()
                .getModContainer("minecraft")
                .orElseThrow()
                .getMetadata()
                .getVersion()
                .getFriendlyString();
        } catch (Exception e) {
            return "";
        }
    }

    static String chooseVariant(String version) {
        Matcher matcher = VERSION_PATTERN.matcher(version);
        if (!matcher.find()) {
            return null;
        }
        int major = Integer.parseInt(matcher.group(1));
        int minor = Integer.parseInt(matcher.group(2));
        int patch = matcher.group(3) == null ? 0 : Integer.parseInt(matcher.group(3));
        if (major == 1 && minor == 21) {
            if (patch < 5) {
                return "legacy121";
            }
            return patch <= 8 ? "legacy125" : null;
        }
        return major >= 26 ? "modern26" : null;
    }
}
