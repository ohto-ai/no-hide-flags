package indi.ohtoai.tool.no_hide_flags.mixin.client;

import net.minecraft.component.type.UnbreakableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.text.Text;

@Mixin(UnbreakableComponent.class)
public interface UnbreakableComponentAccessor {
    @Accessor("TOOLTIP_TEXT")
    static Text getTooltipText() {
        throw new UnsupportedOperationException("This method should not be called directly. Use the accessor instead.");
    }
}
