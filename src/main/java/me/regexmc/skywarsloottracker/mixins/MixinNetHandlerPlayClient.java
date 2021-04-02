package me.regexmc.skywarsloottracker.mixins;

import me.regexmc.skywarsloottracker.InsertableItem;
import me.regexmc.skywarsloottracker.SkywarsLootTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S30PacketWindowItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class MixinNetHandlerPlayClient {

    // Fired twice?

    @Inject(method = "handleWindowItems", at = @At(value = "HEAD"))
    private void handleSetSlot(S30PacketWindowItems packetIn, CallbackInfo ci) {
        if (packetIn != null) {
            final ItemStack[] itemStacks = packetIn.getItemStacks();
            if (itemStacks != null) {
                final Minecraft mc = SkywarsLootTracker.mc;
                if (mc != null) {
                    final EntityPlayer p = mc.thePlayer;
                    if (p != null) {
                        final InventoryPlayer inventory = p.inventory;
                        if (inventory != null) {
                            if (itemStacks.length == 63) {
                                for (int i = 0; i <= 26; i++) { // i <= 26 is chest items
                                    final ItemStack item = itemStacks[i];
                                    if (item != null) {
                                        SkywarsLootTracker.writeItem(
                                                new InsertableItem(
                                                        String.valueOf(Item.getIdFromItem(item.getItem())),
                                                        String.valueOf(item.stackSize),
                                                        item.getItem().getUnlocalizedName()
                                                )
                                        );
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}