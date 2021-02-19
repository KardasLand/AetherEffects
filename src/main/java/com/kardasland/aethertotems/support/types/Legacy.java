package com.kardasland.aethertotems.support.types;

import com.cryptomorin.xseries.XMaterial;
import com.kardasland.aethertotems.support.Support;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class Legacy implements Support {
    @Override
    public XMaterial getXMaterial(String material, int data) {
        Optional<XMaterial> xmaterial = XMaterial.matchXMaterial(material + ":" + data);
        return xmaterial.orElse(null);
    }

    @Override
    public void setItemInHand(Player p, ItemStack stack) {
        p.getInventory().setItemInHand(stack);
    }
}
