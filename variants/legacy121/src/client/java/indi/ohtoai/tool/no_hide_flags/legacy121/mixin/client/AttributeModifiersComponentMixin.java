package indi.ohtoai.tool.no_hide_flags.legacy121.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.component.type.AttributeModifiersComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AttributeModifiersComponent.class)
public abstract class AttributeModifiersComponentMixin {
    @Inject(method = "showInTooltip", at = @At("HEAD"), cancellable = true)
    private void conditionalShow(CallbackInfoReturnable<Boolean> cir) {
        if (MinecraftClient.getInstance() != null && MinecraftClient.getInstance().options.advancedItemTooltips) {
            cir.setReturnValue(true);
        } else {
            cir.setReturnValue(((AttributeModifiersComponentAccessor) this).isShowInTooltip());
        }
    }
}
