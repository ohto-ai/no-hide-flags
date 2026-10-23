package indi.ohtoai.tool.no_hide_flags.modern26;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.List;

public class NoHideFlagsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ItemTooltipCallback.EVENT.register((ItemStack stack, Item.TooltipContext context, TooltipFlag type, List<Component> lines) -> {
            if (!Minecraft.getInstance().options.advancedItemTooltips) {
                return;
            }

            ItemEnchantments enchantments = stack.get(DataComponents.ENCHANTMENTS);
            if (enchantments == null || enchantments.isEmpty()) {
                return;
            }

            TooltipDisplay display = stack.getOrDefault(
                DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT);

            // Vanilla returns an immutable list when the whole tooltip is hidden.
            if (display.hideTooltip()) {
                return;
            }

            if (display.hiddenComponents().contains(DataComponents.ENCHANTMENTS)) {
                lines.add(Component.translatable("tooltip.no_hide_flags.hide_enchantments")
                    .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            }
        });
    }
}
