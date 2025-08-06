package indi.ohtoai.tool.no_hide_flags.mixin.client;

import net.minecraft.component.type.UnbreakableComponent;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.function.Consumer;
import net.minecraft.text.Text;

@Mixin(UnbreakableComponent.class)
public abstract class UnbreakableComponentMixin {
    @Inject(method = "appendTooltip", at = @At("HEAD"), cancellable = true)
    private void onAppendTooltip(Item.TooltipContext context, Consumer<Text> tooltip, TooltipType type, CallbackInfo ci) {
        if (MinecraftClient.getInstance().options.advancedItemTooltips) {
            tooltip.accept(UnbreakableComponentAccessor.getTooltipText());
            ci.cancel();
        }
    }

}
