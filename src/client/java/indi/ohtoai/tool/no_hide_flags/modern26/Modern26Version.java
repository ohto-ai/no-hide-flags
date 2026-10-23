package indi.ohtoai.tool.no_hide_flags.modern26;

import net.fabricmc.loader.api.FabricLoader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Era check for the 26.x variant: the unobfuscated Mojang runtime that
 * replaced the Yarn/intermediary pipeline.
 */
public final class Modern26Version {
    public static final boolean IS_SUPPORTED_ERA;

    private static final Pattern VERSION_PATTERN = Pattern.compile("^(\\d+)\\.(\\d+)(?:\\.(\\d+))?");

    static {
        boolean result = false;
        try {
            String version = FabricLoader.getInstance()
                .getModContainer("minecraft")
                .orElseThrow()
                .getMetadata()
                .getVersion()
                .getFriendlyString();

            Matcher matcher = VERSION_PATTERN.matcher(version);
            if (matcher.find()) {
                result = Integer.parseInt(matcher.group(1)) >= 26;
            }
        } catch (Exception ignored) {
        }
        IS_SUPPORTED_ERA = result;
    }

    private Modern26Version() {
    }
}
