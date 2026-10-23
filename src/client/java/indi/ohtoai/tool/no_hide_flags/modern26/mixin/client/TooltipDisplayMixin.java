package indi.ohtoai.tool.no_hide_flags.modern26.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.component.TooltipDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TooltipDisplay.class)
public abstract class TooltipDisplayMixin {
    @Inject(method = "shows", at = @At("HEAD"), cancellable = true)
    private void forceDisplayWhenAdvanced(DataComponentType<?> component, CallbackInfoReturnable<Boolean> cir) {
        if (Minecraft.getInstance() != null
            && Minecraft.getInstance().options.advancedItemTooltips) {
            cir.setReturnValue(true);
        }
    }
}
