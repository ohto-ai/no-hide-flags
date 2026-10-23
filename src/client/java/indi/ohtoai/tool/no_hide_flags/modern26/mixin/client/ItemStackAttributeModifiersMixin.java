package indi.ohtoai.tool.no_hide_flags.modern26.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.apache.commons.lang3.function.TriConsumer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemStack.class)
public abstract class ItemStackAttributeModifiersMixin {
    @Redirect(
        method = "addAttributeTooltips",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;forEachModifier(Lnet/minecraft/world/entity/EquipmentSlotGroup;Lorg/apache/commons/lang3/function/TriConsumer;)V"
        )
    )
    private void showHiddenAttribute(ItemStack stack, EquipmentSlotGroup group,
                                     TriConsumer<Holder<Attribute>, AttributeModifier, ItemAttributeModifiers.Display> consumer) {
        boolean advanced = Minecraft.getInstance() != null
            && Minecraft.getInstance().options.advancedItemTooltips;

        stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY)
            .forEach(group, (attribute, modifier, display) -> consumer.accept(
                attribute,
                modifier,
                advanced && display == ItemAttributeModifiers.Display.hidden()
                    ? ItemAttributeModifiers.Display.attributeModifiers()
                    : display
            ));
    }
}
