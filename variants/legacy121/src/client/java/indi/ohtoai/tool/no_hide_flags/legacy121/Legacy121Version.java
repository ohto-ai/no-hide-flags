package indi.ohtoai.tool.no_hide_flags.legacy121;

import net.fabricmc.loader.api.FabricLoader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Era check for the 1.21 - 1.21.4 variant: this code path talks to the
 * per-component {@code showInTooltip} flags that were replaced by the
 * TooltipDisplay component in 1.21.5.
 */
public final class Legacy121Version {
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
                int major = Integer.parseInt(matcher.group(1));
                int minor = Integer.parseInt(matcher.group(2));
                int patch = matcher.group(3) == null ? 0 : Integer.parseInt(matcher.group(3));
                result = major == 1 && minor == 21 && patch < 5;
            }
        } catch (Exception ignored) {
        }
        IS_SUPPORTED_ERA = result;
    }

    private Legacy121Version() {
    }
}
