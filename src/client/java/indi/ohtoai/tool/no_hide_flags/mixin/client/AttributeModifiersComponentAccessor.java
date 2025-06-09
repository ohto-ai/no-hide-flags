package indi.ohtoai.tool.no_hide_flags.mixin.client;

import net.minecraft.component.type.AttributeModifiersComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AttributeModifiersComponent.class)
public interface AttributeModifiersComponentAccessor {
    @Accessor("showInTooltip")
    boolean isShowInTooltip();
}
