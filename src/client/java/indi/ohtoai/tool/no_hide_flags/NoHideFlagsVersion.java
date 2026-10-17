package indi.ohtoai.tool.no_hide_flags;

import net.fabricmc.loader.api.FabricLoader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class NoHideFlagsVersion {
    public static final boolean IS_1_21_6_OR_LATER;

    private static final Pattern VERSION_PATTERN = Pattern.compile("^(\\d+)\\.(\\d+)(?:\\.(\\d+))?");

    static {
        boolean result = true;
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
                result = major > 1 || (major == 1 && (minor > 21 || (minor == 21 && patch >= 6)));
            }
        } catch (Exception ignored) {
        }
        IS_1_21_6_OR_LATER = result;
    }

    private NoHideFlagsVersion() {
    }
}
