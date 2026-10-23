package indi.ohtoai.tool.no_hide_flags.modern26.mixin.client;

import indi.ohtoai.tool.no_hide_flags.modern26.Modern26Version;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

/**
 * Keeps this variant dormant outside 26.x: the mixin targets use Mojang
 * names that do not resolve on intermediary runtimes.
 */
public class Modern26MixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return Modern26Version.IS_SUPPORTED_ERA;
    }

    @Override
    public List<String> getMixins() {
        return Modern26Version.IS_SUPPORTED_ERA
            ? List.of("TooltipDisplayMixin", "ItemStackAttributeModifiersMixin")
            : List.of();
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
