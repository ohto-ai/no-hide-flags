package indi.ohtoai.tool.no_hide_flags.mixin.client;

import indi.ohtoai.tool.no_hide_flags.NoHideFlagsVersion;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class NoHideFlagsMixinPlugin implements IMixinConfigPlugin {
    private static final String ITEM_STACK_MIXIN =
        "indi.ohtoai.tool.no_hide_flags.mixin.client.ItemStackAttributeModifiersMixin";

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.equals(ITEM_STACK_MIXIN)) {
            return NoHideFlagsVersion.IS_1_21_6_OR_LATER;
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return NoHideFlagsVersion.IS_1_21_6_OR_LATER
            ? List.of("ItemStackAttributeModifiersMixin")
            : List.of();
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
