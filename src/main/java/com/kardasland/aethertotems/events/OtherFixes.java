package com.kardasland.aethertotems.events;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;

public class OtherFixes implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void checkCraft(CraftItemEvent e){
        for (ItemStack i : e.getInventory().getContents()){
            if (NBTEditor.contains(i, "totem_type") || NBTEditor.contains(i, "fuel_type")){
                e.getInventory().setResult(new ItemStack(Material.AIR));
            }
        }
    }
    @EventHandler(ignoreCancelled = true)
    public void furnaceBurnCheck(FurnaceBurnEvent e){
            ItemStack i = e.getFuel();
            if (NBTEditor.contains(i, "totem_type") || NBTEditor.contains(i, "fuel_type")){
                e.setBurning(false);
                e.setCancelled(true);
            }
    }
    @EventHandler(ignoreCancelled = true)
    public void furnaceSmeltCheck(FurnaceSmeltEvent e){
        ItemStack i = e.getSource();
        if (NBTEditor.contains(i, "totem_type") || NBTEditor.contains(i, "fuel_type")){
            e.setCancelled(true);
            e.setResult(new ItemStack(Material.AIR));
        }
    }
    @EventHandler(ignoreCancelled = true)
    public void anvilCheck(PrepareAnvilEvent e){
        for (ItemStack i : e.getInventory().getContents()){
            if (NBTEditor.contains(i, "totem_type") || NBTEditor.contains(i, "fuel_type")){
                e.setResult(new ItemStack(Material.AIR));
                Player p = (Player) e.getView().getPlayer();
                p.closeInventory();
            }
        }
    }
    @EventHandler(ignoreCancelled = true)
    public void enchant(EnchantItemEvent e){
        ItemStack i = e.getItem();
        if (NBTEditor.contains(i, "totem_type") || NBTEditor.contains(i, "fueltype")){
            e.setCancelled(true);
            Player p = e.getEnchanter();
            p.closeInventory();
        }
    }
}
