package indi.ohtoai.tool.no_hide_flags.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemStack.class)
public abstract class ItemStackAttributeModifiersMixin {
    // Vanilla skips hidden attribute entries before their tooltip is rendered.
    @ModifyVariable(method = "method_57370", at = @At("HEAD"), argsOnly = true)
    private static AttributeModifiersComponent.Display showHiddenAttribute(AttributeModifiersComponent.Display display) {
        if (display == AttributeModifiersComponent.Display.getHidden()
            && MinecraftClient.getInstance() != null
            && MinecraftClient.getInstance().options.advancedItemTooltips) {
            return AttributeModifiersComponent.Display.getDefault();
        }
        return display;
    }
}
