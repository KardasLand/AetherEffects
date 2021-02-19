package com.kardasland.aethertotems.menu;


import com.cryptomorin.xseries.XMaterial;
import com.kardasland.aethertotems.AetherTotems;
import com.kardasland.aethertotems.support.Support;
import com.kardasland.aethertotems.totem.Totem;
import com.kardasland.aethertotems.utils.Araclar;
import com.kardasland.aethertotems.utils.ConfigManager;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainMenu implements InventoryProvider {
    private final Totem totem;
    private ItemStack totemItem;
    private Player player;
    public Support support = AetherTotems.instance.getSupport();
    Araclar araclar = new Araclar();

    public MainMenu(ItemStack item) {
        this.totem = new Totem(NBTEditor.getString(item, "totem_type"));
        this.totemItem = item;
    }

    // x,y
    @Override
    public void init(Player player, InventoryContents contents) {
                contents.fill(ClickableItem.empty(parseItem("Filler", totemItem, totem)));
                contents.set(1,2, ClickableItem.empty(parseItem("Totem-Effect-Info", totemItem, totem)));
                contents.set(1,6, ClickableItem.of(parseItem("Change-Activity", totemItem, totem), e->
                        changeActivity(player, totemItem)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        contents.set(1,4, ClickableItem.of(parseItem("Add-Fuel", totemItem, totem), e->
                addFuel(player, totemItem)));
    }
    public void addFuel(Player p, ItemStack olditem){
        ItemStack fuel = null;
        for (ItemStack i : p.getInventory().getContents()){
            if (i != null){
                if (NBTEditor.contains(i, "fuel_type")){
                    if (NBTEditor.getString(i, "fuel_type").equals(totem.getTotemtype())){
                        fuel = i;
                        break;
                    }
                }
            }
        }
        if (fuel == null){
            araclar.prefix(p, ConfigManager.get("messages.yml").getString("No-Fuel"));
        }else {
            ItemStack newitem = olditem;
            int fuel_amount = NBTEditor.getInt(olditem, "totem_fuel_amount");
            int cap = totem.getFuel_cap();
            if (fuel_amount + 1 > cap){
                //cancel
                araclar.prefix(p, ConfigManager.get("messages.yml").getString("Exceeded-Fuel-Cap").replace("%kapasite%", String.valueOf(cap)));
            }else {
                if (fuel.getAmount() == 1){
                    p.getInventory().remove(fuel);
                    p.updateInventory();
                }else {
                    ItemStack fuelnew = fuel;
                    fuelnew.setAmount(fuel.getAmount() - 1);
                    araclar.replace(p, fuel, fuelnew);
                }
                fuel_amount += 1;
                newitem = NBTEditor.set(newitem, fuel_amount, "totem_fuel_amount");
                newitem = NBTEditor.set(newitem, 20, "totem_fuel_time");
                newitem = araclar.updateLore(newitem);
                support.setItemInHand(p, newitem);
                p.updateInventory();
                p.closeInventory();
                araclar.prefix(p, ConfigManager.get("messages.yml").getString("Added-Fuel"));
            }

        }
        
    }
    public void changeActivity(Player p, ItemStack olditem){
        int fuel_amount = NBTEditor.getInt(olditem, "totem_fuel_amount");
        if (fuel_amount == 0){
            araclar.prefix(p, ConfigManager.get("messages.yml").getString("No-Fuel"));
        }else {
            ItemStack newitem = olditem;
            boolean newstate = NBTEditor.getBoolean(olditem, "totem_active");
            newitem = NBTEditor.set(newitem, !newstate, "totem_active");
            support.setItemInHand(p, newitem);
            araclar.prefix(p, ConfigManager.get("messages.yml").getString("Successfully-Switched").replace("%durum%", (!newstate ? "açık" : "kapalı")));
            p.closeInventory();
        }
    }
    //new Araclar().replace();
    public ItemStack parseItem(String path, ItemStack s, Totem totem){
        int fuel_amount = NBTEditor.getInt(s, "totem_fuel_amount");
        if (path.equals("Filler")){
            ItemStack item = XMaterial.BLACK_STAINED_GLASS_PANE.parseItem();
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(" ");
            item.setItemMeta(itemMeta);
            return item;
        }else if (path.equals("Totem-Effect-Info")){
            ItemStack item = support.getXMaterial(ConfigManager.get("config.yml").getString("GUI.Totem-Effect-Info.material.type"), ConfigManager.get("config.yml").getInt("GUI.Totem-Effect-Info.material.data")).parseItem();
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(Araclar.color(ConfigManager.get("config.yml").getString("GUI.Totem-Effect-Info.Title")));
            List<String> lore = new ArrayList<>();
            for (String key : ConfigManager.get("config.yml").getStringList("GUI.Totem-Effect-Info.Effects-Placeholder")){
                for (Map.Entry<PotionEffectType, Integer> map : totem.getEffectTypeMap().entrySet()){
                    lore.add(key.replace("%level%", String.valueOf(map.getValue()))
                    .replace("%effect%", ConfigManager.get("i18n.yml").getString(map.getKey().getName())));
                }
            }
            itemMeta.setLore(Araclar.color(lore));
            item.setItemMeta(itemMeta);
            return item;
        }else if (path.equals("Add-Fuel")){
            ItemStack item = support.getXMaterial(ConfigManager.get("config.yml").getString("GUI.Add-Fuel.material.type"), ConfigManager.get("config.yml").getInt("GUI.Add-Fuel.material.data")).parseItem();
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(Araclar.color(ConfigManager.get("config.yml").getString("GUI.Add-Fuel.Title")));
            List<String> lore = new ArrayList<>();
            for (String key : ConfigManager.get("config.yml").getStringList("GUI.Add-Fuel.Lore")){
                lore.add(key
                        .replace("%yakilanyakit%", String.valueOf(totem.getTake_fuel_amount()))
                        .replace("%maxyakit%", String.valueOf(totem.getFuel_cap()))
                        .replace("%yakitsure%", String.valueOf(totem.getFuel_duration()))
                        .replace("%mevcutyakit%", String.valueOf(fuel_amount))
                );
            }
            itemMeta.setLore(Araclar.color(lore));
            item.setItemMeta(itemMeta);
            return item;
        }else {
            if (NBTEditor.getBoolean(totemItem, "totem_active")){
                ItemStack item = support.getXMaterial(ConfigManager.get("config.yml").getString("GUI.Change-Activity.Active.material.type"), ConfigManager.get("config.yml").getInt("GUI.Change-Activity.Active.material.data")).parseItem();
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.setDisplayName(Araclar.color(ConfigManager.get("config.yml").getString("GUI.Change-Activity.Active.Title")));
                itemMeta.setLore(Araclar.color(ConfigManager.get("config.yml").getStringList("GUI.Change-Activity.Active.Lore")));
                item.setItemMeta(itemMeta);
                return item;
            }else {
                ItemStack item = support.getXMaterial(ConfigManager.get("config.yml").getString("GUI.Change-Activity.Inactive.material.type"), ConfigManager.get("config.yml").getInt("GUI.Change-Activity.Inactive.material.data")).parseItem();
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.setDisplayName(Araclar.color(ConfigManager.get("config.yml").getString("GUI.Change-Activity.Inactive.Title")));
                itemMeta.setLore(Araclar.color(ConfigManager.get("config.yml").getStringList("GUI.Change-Activity.Inactive.Lore")));
                item.setItemMeta(itemMeta);
                return item;
            }
        }
    }

}
