package com.kardasland.aethertotems.utils;

import com.kardasland.aethertotems.AetherTotems;
import com.kardasland.aethertotems.totem.TotemItem;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Araclar {
    public static String color(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }
    public static List<String> color(List<String> list){
        List<String> temp = new ArrayList<>();
        for (String key : list){
            temp.add(color(key));
        }
        return temp;
    }
    public void replace(Player p, ItemStack olditem, ItemStack newitem) {
        for(int i = 0; i<p.getInventory().getSize()-1; ++i) {
            ItemStack item = p.getInventory().getItem(i);
            if (item != null){
                if(item.equals(olditem)) {
                    p.getInventory().setItem(i, newitem);
                    p.updateInventory();
                    return;
                }
            }
        }
    }
    public void noPerms(Player p, String placeholder){
        prefix(p, ConfigManager.get("messages.yml").getString("Not-Enough-Permissions").replace("%yetki%", placeholder));
    }
    public void nonPrefix(Player p, String s){
        p.sendMessage(color(s));
    }
    public void nonPrefix(String s){
        Bukkit.getLogger().info(color(s));
    }
    public void prefix(String s){
        AetherTotems.instance.getLogger().info(color(s));
    }
    public ItemStack updateLore(ItemStack item){
        int amount = NBTEditor.getInt(item,  "totem_fuel_amount");
        int time = NBTEditor.getInt(item, "totem_fuel_time");
        ItemMeta itemMeta = item.getItemMeta();
        TotemItem totemItem = new TotemItem(item);
        List<String> temp = new ArrayList<>();
        for (String key : totemItem.getLore()){
            temp.add(key
            .replace("%mevcutyakit%", String.valueOf(amount))
            .replace("%kalansure%", String.valueOf(time))
            );
        }
        itemMeta.setLore(color(temp));
        item.setItemMeta(itemMeta);
        return item;
    }
    public String getPrefix(){
        return ConfigManager.get("config.yml").getString("Prefix");
    }
    public void prefix(Player p, String s){
        p.sendMessage(color(getPrefix() + s));
    }
}
