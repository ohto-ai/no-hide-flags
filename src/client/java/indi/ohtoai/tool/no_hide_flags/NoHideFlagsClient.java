package indi.ohtoai.tool.no_hide_flags;

import indi.ohtoai.tool.no_hide_flags.mixin.client.AttributeModifiersComponentAccessor;
import indi.ohtoai.tool.no_hide_flags.mixin.client.AttributeModifiersComponentMixin;
import indi.ohtoai.tool.no_hide_flags.mixin.client.ItemEnchantmentsComponentAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.tooltip.TooltipAppender;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

public class NoHideFlagsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ItemTooltipCallback.EVENT.register((ItemStack stack, Item.TooltipContext context, TooltipType type, List<Text> lines) -> {
            if (!MinecraftClient.getInstance().options.advancedItemTooltips) {
                return;
            }

            ItemEnchantmentsComponent enchantmentsComponent = stack.get(DataComponentTypes.ENCHANTMENTS);

            if (enchantmentsComponent != null
                && !enchantmentsComponent.isEmpty()
                && !((ItemEnchantmentsComponentAccessor) enchantmentsComponent).isShowInTooltip()) {
                lines.add(Text.literal("Hide Enchantments")
                    .formatted(Formatting.GRAY, Formatting.ITALIC));
                enchantmentsComponent.getEnchantments().forEach(enchantment -> {
                    lines.add(Enchantment.getName(enchantment,
                        enchantmentsComponent.getLevel(enchantment)
                    ));
                });
            }
        });
    }
}