package com.kardasland.aethertotems.events;

import com.kardasland.aethertotems.totem.Totem;
import com.kardasland.aethertotems.totem.TotemItem;
import com.kardasland.aethertotems.utils.Araclar;
import com.kardasland.aethertotems.utils.ConfigManager;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Map;

public class TotemCheckRunnable extends BukkitRunnable {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()){
            try {
                Arrays.stream(player.getInventory().getContents()).filter(olditem -> olditem != null && NBTEditor.contains(olditem, "totem_active")).forEach(olditem -> {
                    ItemStack item = olditem;
                    if (NBTEditor.getBoolean(item, "totem_active")){
                        String totem_type = NBTEditor.getString(item, "totem_type");
                        Totem totem = new Totem(totem_type);
                        int fuel_time = NBTEditor.getInt(item, "totem_fuel_time");
                        int fuel_amount = NBTEditor.getInt(item, "totem_fuel_amount");
                        //Bukkit.broadcastMessage(String.valueOf(fuel_time));
                        //Bukkit.broadcastMessage(String.valueOf(fuel_amount));
                        fuel_time -= 1;
                        if (fuel_time <= 0){
                            fuel_amount -= totem.getTake_fuel_amount();
                            if (fuel_amount >= 0){
                                fuel_time = 20;
                            }else {
                                fuel_time = 0;
                                fuel_amount = 0;
                                item = NBTEditor.set(item, false, "totem_active");
                                new Araclar().prefix(player, ConfigManager.get("messages.yml").getString("No-Fuel-Left").replace("%totem%", totem.getTotemtype()));
                            }
                        }
                        //TotemItem totemItem = new TotemItem(totem);
                        //ItemMeta itemMeta = item.getItemMeta();
                        //itemMeta.setDisplayName(String.valueOf(fuel_time));
                        //item.setItemMeta(itemMeta);
                        item = NBTEditor.set(item, fuel_amount, "totem_fuel_amount");
                        item = NBTEditor.set(item, fuel_time, "totem_fuel_time");
                        item = new Araclar().updateLore(item);
                        new Araclar().replace(player, olditem, item);
                        for (Map.Entry<PotionEffectType, Integer> map : totem.getEffectTypeMap().entrySet()){
                            player.addPotionEffect(new PotionEffect(map.getKey(), 45, map.getValue() - 1));
                        }
                    }
                });
            }catch (IllegalArgumentException ignored){

            }
        }
    }
}
