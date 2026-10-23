package indi.ohtoai.tool.no_hide_flags.legacy125;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class NoHideFlagsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ItemTooltipCallback.EVENT.register((ItemStack stack, Item.TooltipContext context, TooltipType type, List<Text> lines) -> {
            if (!MinecraftClient.getInstance().options.advancedItemTooltips) {
                return;
            }

            ItemEnchantmentsComponent enchantments = stack.get(DataComponentTypes.ENCHANTMENTS);
            if (enchantments == null || enchantments.isEmpty()) {
                return;
            }

            TooltipDisplayComponent display = stack.getOrDefault(
                DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplayComponent.DEFAULT);

            // Vanilla returns an immutable list when the whole tooltip is hidden.
            if (display.hideTooltip()) {
                return;
            }

            if (display.hiddenComponents().contains(DataComponentTypes.ENCHANTMENTS)) {
                lines.add(Text.translatable("tooltip.no_hide_flags.hide_enchantments")
                    .formatted(Formatting.GRAY, Formatting.ITALIC));
            }
        });
    }
}
