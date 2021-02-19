package com.kardasland.aethertotems.totem;

import com.cryptomorin.xseries.XMaterial;
import com.kardasland.aethertotems.AetherTotems;
import com.kardasland.aethertotems.utils.Araclar;
import com.kardasland.aethertotems.utils.ConfigManager;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class TotemFactory {
    private String type;
    private String totemid;
    private int amount;
    private String material;
    private int material_data;
    private ItemStack base;
    private String name;
    private List<String> lore;
    private int fuel_duration;
    private boolean active = false;
    private int fuel_amount = 1;

    public TotemFactory(String type, String totemid, int amount) throws NullPointerException{
        FileConfiguration cf = ConfigManager.get("totems.yml");
        this.totemid = totemid;
        this.type = type;
        if (type.equals("fuel")){
            this.fuel_duration = cf.getInt("Totems."+totemid+".fuel.duration");
            this.amount = amount;
            this.name = cf.getString("Totems."+totemid+".fuel.name");
            this.lore = cf.getStringList("Totems."+totemid+".fuel.lore");
            this.base = AetherTotems.instance.getSupport().getXMaterial(cf.getString("Totems."+totemid+".fuel.material.type"), cf.getInt("Totems."+totemid+".fuel.material.data")).parseItem();
        }else {
            TotemItem totemItem = new TotemItem(totemid);
            this.name = totemItem.getTitle();
            this.lore = totemItem.getLore();
            this.base = totemItem.toItemStack();
            this.amount = amount;
        }
    }

    public ItemStack toItemStack(){
        if (type.equals("totem")){
            base = NBTEditor.set(base, fuel_amount, "totem_fuel_amount");
            base = NBTEditor.set(base, totemid, "totem_type");
            base = NBTEditor.set(base, fuel_duration, "totem_fuel_time");
            base = NBTEditor.set(base, active, "totem_active");
            base.setAmount(amount);
        }else {
            base = NBTEditor.set(base, totemid, "fuel_type");
            base.setAmount(amount);
        }
        ItemMeta itemMeta = base.getItemMeta();
        itemMeta.setDisplayName(Araclar.color(name));
        itemMeta.setLore(Araclar.color(lore));
        base.setItemMeta(itemMeta);
        base = new Araclar().updateLore(base);
        return base;
    }


}
