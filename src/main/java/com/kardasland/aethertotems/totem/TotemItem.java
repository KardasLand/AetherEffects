package com.kardasland.aethertotems.totem;

import com.cryptomorin.xseries.XMaterial;
import com.kardasland.aethertotems.AetherTotems;
import com.kardasland.aethertotems.utils.ConfigManager;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Optional;

public class TotemItem {
    private String title;
    private String totemtype;
    private List<String> lore;
    
    private String material;
    private int material_data;
    
    private int fuel_amount;
    private int fuel_time;
    
    
    public TotemItem(String totemtype){
        FileConfiguration cf = ConfigManager.get("totems.yml");
        this.title = cf.getString("Totems."+totemtype+".material.name");
        this.lore = cf.getStringList("Totems."+totemtype+".material.lore");
        this.material = (cf.getString("Totems."+totemtype+".material.type"));
        this.material_data = (cf.getInt("Totems."+totemtype+".material.data"));
    }

    public TotemItem(Totem totem){
        FileConfiguration cf = ConfigManager.get("totems.yml");
        this.title = cf.getString("Totems."+totem.getTotemtype()+".material.name");
        this.lore = cf.getStringList("Totems."+totem.getTotemtype()+".material.lore");
    }

    public TotemItem(ItemStack itemStack){
        this.totemtype = NBTEditor.getString(itemStack, "totem_type");
        this.fuel_time = NBTEditor.getInt(itemStack, "totem_fuel_time");
        this.fuel_amount = NBTEditor.getInt(itemStack, "totem_fuel_amount");
        FileConfiguration cf = ConfigManager.get("totems.yml");
        this.title = cf.getString("Totems."+totemtype+".material.name");
        this.lore = cf.getStringList("Totems."+totemtype+".material.lore");
        this.material = (cf.getString("Totems."+totemtype+".material.type"));
        this.material_data = (cf.getInt("Totems."+totemtype+".material.data"));
    }
    public ItemStack toItemStack(){
        XMaterial xmaterial = AetherTotems.instance.getSupport().getXMaterial(material, material_data);
        return xmaterial.parseItem();
    }
    public String getTotemtype() {
        return totemtype;
    }

    public List<String> getLore() {
        return lore;
    }

    public String getTitle() {
        return title;
    }

    public String getMaterial() {
        return material;
    }

    public int getFuel_amount() {
        return fuel_amount;
    }

    public int getMaterial_data() {
        return material_data;
    }

    public int getFuel_time() {
        return fuel_time;
    }
}
