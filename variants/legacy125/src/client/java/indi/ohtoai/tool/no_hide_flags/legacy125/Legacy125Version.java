package indi.ohtoai.tool.no_hide_flags.legacy125;

import net.fabricmc.loader.api.FabricLoader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Era check for the 1.21.5 - 1.21.8 variant. Within the era, 1.21.5 lacks
 * {@code AttributeModifiersComponent.Display}, so the attribute-revealing
 * mixin is only registered on 1.21.6+ (same gating the standalone v1.3.0
 * releases used).
 */
public final class Legacy125Version {
    public static final boolean IS_SUPPORTED_ERA;
    public static final boolean IS_1_21_6_OR_LATER;

    private static final Pattern VERSION_PATTERN = Pattern.compile("^(\\d+)\\.(\\d+)(?:\\.(\\d+))?");

    static {
        boolean era = false;
        boolean later = true;
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
                era = major == 1 && minor == 21 && patch >= 5 && patch <= 8;
                later = major > 1 || (major == 1 && (minor > 21 || (minor == 21 && patch >= 6)));
            }
        } catch (Exception ignored) {
        }
        IS_SUPPORTED_ERA = era;
        IS_1_21_6_OR_LATER = later;
    }

    private Legacy125Version() {
    }
}
