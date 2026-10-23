package indi.ohtoai.tool.no_hide_flags.legacy121.mixin.client;

import indi.ohtoai.tool.no_hide_flags.legacy121.Legacy121Version;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

/**
 * Keeps this variant dormant outside 1.21 - 1.21.4: the mixin targets do not
 * exist on other runtimes, and the classes must never be listed (let alone
 * loaded) there.
 */
public class Legacy121MixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return Legacy121Version.IS_SUPPORTED_ERA;
    }

    @Override
    public List<String> getMixins() {
        return Legacy121Version.IS_SUPPORTED_ERA
            ? List.of(
                "AttributeModifiersComponentAccessor",
                "ItemEnchantmentsComponentAccessor",
                "AttributeModifiersComponentMixin")
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
