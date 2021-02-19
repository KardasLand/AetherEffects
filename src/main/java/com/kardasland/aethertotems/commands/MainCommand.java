package com.kardasland.aethertotems.commands;

import com.cryptomorin.xseries.XMaterial;
import com.kardasland.aethertotems.AetherTotems;
import com.kardasland.aethertotems.totem.TotemFactory;
import com.kardasland.aethertotems.utils.Araclar;
import com.kardasland.aethertotems.utils.ConfigManager;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class MainCommand implements CommandExecutor {
    Araclar araclar = new Araclar();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player p = (Player) sender;
            if (args.length == 0){
                defaultScreen(p);
            }else if (args.length == 1){
                if (args[0].equals("yenile")|| args[0].equals("reload")){
                    if (p.hasPermission("aethertotems.reload")){
                        reloadCfgs();
                        araclar.prefix(p, "Başarıyla yeniledin.");
                    }else {
                        araclar.noPerms(p, "aethertotems.reload");
                    }
                }else if (args[0].equals("liste")){
                    if (p.hasPermission("aethertotems.list")){
                        araclar.prefix(p, "Totem listesi: " + totemList());
                    }else {
                        araclar.noPerms(p, "aethertotems.list");
                    }
                }else {
                    helpScreen(p);
                }
            }else if (args.length == 5){
                if (args[0].equals("give")){
                    if (p.hasPermission("aethertotems.give")){
                        try {
                            Player target = Bukkit.getPlayer(args[1]);
                            if (target != null && target.isOnline()){
                                TotemFactory totemFactory = new TotemFactory(args[2], args[3], Integer.parseInt(args[4]));
                                ItemStack item = totemFactory.toItemStack();
                                target.getInventory().addItem(item);
                                araclar.prefix(p, ConfigManager.get("messages.yml").getString("Successfully-Given").replace("%hedef%", target.getName()).replace("%item%", args[4]));
                            }else {
                                araclar.prefix(p, "Kullanıcı inaktif.");
                            }
                        }catch (NullPointerException | IllegalArgumentException ex){
                            araclar.prefix(p, "Hatalı veya eksik girdiniz. ");
                        }
                    }else {
                        araclar.noPerms(p, "aethertotems.give");
                    }
                }
            }
        }else {
            if (args.length == 0){
                defaultScreen();
            }else if (args.length == 1){
                if (args[0].equals("yenile")|| args[0].equals("reload")){
                    reloadCfgs();
                    araclar.prefix( "Başarıyla yeniledin.");
                }else if (args[0].equals("liste")){
                    araclar.prefix("Totem listesi: " + totemList());
                }else {
                    helpScreen();
                }
            }else if (args.length == 5){
                if (args[0].equals("give")){
                    try {
                        Player target = Bukkit.getPlayer(args[1]);
                        if (target != null && target.isOnline()){
                            TotemFactory totemFactory = new TotemFactory(args[2], args[3], Integer.parseInt(args[4]));
                            ItemStack item = totemFactory.toItemStack();
                            target.getInventory().addItem(item);
                            araclar.prefix( ConfigManager.get("messages.yml").getString("Successfully-Given").replace("%hedef%", target.getName()).replace("%item%", args[4]));
                        }else {
                            araclar.prefix( "Kullanıcı inaktif.");
                        }
                    }catch (NullPointerException | IllegalArgumentException ex){
                        araclar.prefix( "Hatalı veya eksik girdiniz. ");
                    }
                }
            }
        }
        return true;
        // /totems give kardasland fuel madenci 10
    }
    public void reloadCfgs(){
        ConfigManager.reload("i18n.yml");
        ConfigManager.reload("totems.yml");
        ConfigManager.reload("messages.yml");
        ConfigManager.reload("config.yml");

    }
    public String totemList(){
        StringBuilder sm = new StringBuilder();
        for (String key : ConfigManager.get("totems.yml").getConfigurationSection("Totems.").getKeys(false)){
            sm.append(key).append(", ");
        }
        return sm.substring(0, sm.length() - 2) + ".";

    }

    public void defaultScreen(){
        araclar.nonPrefix("&bAetherEffects &7(&f"+ AetherTotems.instance.getDescription().getVersion()+"&7)");
        araclar.nonPrefix("&7Author: &bKardasLand");
        araclar.nonPrefix("&7Help: &b/aethertotems &fhelp");
    }
    public void defaultScreen(Player p){
        araclar.nonPrefix(p,"&bAetherEffects &7(&f"+ AetherTotems.instance.getDescription().getVersion()+"&7)");
        araclar.nonPrefix(p,"&7Author: &bKardasLand");
        araclar.nonPrefix(p,"&7Help: &b/aethertotems &fhelp");
    }

    public void helpScreen(Player p){
        if (!p.hasPermission("aethertotems.admin")){
            defaultScreen(p);
        }else{
            araclar.nonPrefix(p,"&bAetherEffects &7(&f"+ AetherTotems.instance.getDescription().getVersion()+"&7)");
            araclar.nonPrefix(p," ");
            araclar.nonPrefix(p,"&b/aethertotems yenile");
            araclar.nonPrefix(p,"&7> Eklentiyi yeniler.");
            araclar.nonPrefix(p,"&b/aethertotems liste");
            araclar.nonPrefix(p,"&7> Totem listesini verir.");
            araclar.nonPrefix(p,"&b/aethertotems give <player> <totem/fuel> <totemid> <amount>");
            araclar.nonPrefix(p,"&7> Oyuncuya totem veya yakıt verir.");
        }
    }

    public void helpScreen(){
        araclar.nonPrefix("&bAetherEffects &7(&f"+ AetherTotems.instance.getDescription().getVersion()+"&7)");
        araclar.nonPrefix(" ");
        araclar.nonPrefix("&b/aethertotems yenile");
        araclar.nonPrefix("&7> Eklentiyi yeniler.");
        araclar.nonPrefix("&b/aethertotems liste");
        araclar.nonPrefix("&7> Totem listesini verir.");
        araclar.nonPrefix("&b/aethertotems give <player> <totem/fuel> <totemid> <amount>");
        araclar.nonPrefix("&7> Oyuncuya totem veya yakıt verir.");

    }
}
