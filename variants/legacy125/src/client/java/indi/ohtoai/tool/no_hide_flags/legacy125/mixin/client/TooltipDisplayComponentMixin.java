package indi.ohtoai.tool.no_hide_flags.legacy125.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.TooltipDisplayComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TooltipDisplayComponent.class)
public abstract class TooltipDisplayComponentMixin {
    @Inject(method = "shouldDisplay", at = @At("HEAD"), cancellable = true)
    private void forceDisplayWhenAdvanced(ComponentType<?> component, CallbackInfoReturnable<Boolean> cir) {
        if (MinecraftClient.getInstance() != null
            && MinecraftClient.getInstance().options.advancedItemTooltips) {
            cir.setReturnValue(true);
        }
    }
}
