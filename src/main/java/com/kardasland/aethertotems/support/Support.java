package com.kardasland.aethertotems.support;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Support {
    public XMaterial getXMaterial(String material, int data);
    public void setItemInHand(Player p, ItemStack stack);
}
