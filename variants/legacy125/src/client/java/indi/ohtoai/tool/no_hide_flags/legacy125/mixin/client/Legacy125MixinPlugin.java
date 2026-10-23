package indi.ohtoai.tool.no_hide_flags.legacy125.mixin.client;

import indi.ohtoai.tool.no_hide_flags.legacy125.Legacy125Version;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

/**
 * Keeps this variant dormant outside 1.21.5 - 1.21.8. Within the era the
 * attribute mixin additionally requires 1.21.6+ because Display did not
 * exist in 1.21.5.
 */
public class Legacy125MixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return Legacy125Version.IS_SUPPORTED_ERA;
    }

    @Override
    public List<String> getMixins() {
        if (!Legacy125Version.IS_SUPPORTED_ERA) {
            return List.of();
        }
        return Legacy125Version.IS_1_21_6_OR_LATER
            ? List.of("TooltipDisplayComponentMixin", "ItemStackAttributeModifiersMixin")
            : List.of("TooltipDisplayComponentMixin");
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
