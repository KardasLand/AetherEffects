package com.kardasland.aethertotems.events;

import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;

public class ResurrectFix implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void bugfix(EntityResurrectEvent e){
        if (e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            if (p.getInventory().getItemInMainHand() != null || p.getInventory().getItemInOffHand() != null){
                if (NBTEditor.contains(p.getInventory().getItemInMainHand(), "totem_type") || NBTEditor.contains(p.getInventory().getItemInOffHand(), "totem_type")){
                    e.setCancelled(true);
                    p.setHealth(0);
                }
            }
        }
    }
}
