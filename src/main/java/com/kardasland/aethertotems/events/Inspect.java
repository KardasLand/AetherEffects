package com.kardasland.aethertotems.events;

import com.kardasland.aethertotems.menu.MainMenu;
import com.kardasland.aethertotems.utils.Araclar;
import com.kardasland.aethertotems.utils.ConfigManager;
import fr.minuskube.inv.SmartInventory;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class Inspect implements Listener {
    @EventHandler
    public void check(PlayerInteractEvent ex){
        if (ex.getAction().equals(Action.RIGHT_CLICK_AIR)){
            if (ex.getItem() != null){
                if (NBTEditor.contains(ex.getItem(), "totem_type")){
                    SmartInventory.builder().id("totemmenu")
                            .size(3,9)
                            .provider(new MainMenu(ex.getItem()))
                            .title(Araclar.color(ConfigManager.get("config.yml").getString("GUI.Main-Title")))
                            .build().open(ex.getPlayer());
                }
            }
        }
    }
}
