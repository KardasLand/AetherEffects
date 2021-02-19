package com.kardasland.aethertotems;

import com.kardasland.aethertotems.commands.MainCommand;
import com.kardasland.aethertotems.events.Inspect;
import com.kardasland.aethertotems.events.OtherFixes;
import com.kardasland.aethertotems.events.ResurrectFix;
import com.kardasland.aethertotems.events.TotemCheckRunnable;
import com.kardasland.aethertotems.support.Support;
import com.kardasland.aethertotems.support.types.Legacy;
import com.kardasland.aethertotems.support.types.New;
import com.kardasland.aethertotems.utils.ConfigManager;
import com.kardasland.aethertotems.utils.ProbabilityHandler;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

public final class AetherTotems extends JavaPlugin {
    public static AetherTotems instance;
    private Support support;

    @Override
    public void onEnable() {
        instance = this;
        ConfigManager.load("license.yml");
        if (!new ProbabilityHandler(ConfigManager.get("license.yml").getString("LicenseKey"), "http://www.kardasland.com/license/verify.php", this).setSecurityKey("YecoF0I9182naML9281HuW8iUhTdIUInjkfF").setConsoleLog(ProbabilityHandler.LogType.NONE).register()){
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }else {
            loadConfigs();
            loadSupport();
            loadCommand();
            loadEvents();
            new TotemCheckRunnable().runTaskTimer(this, 20L, 20L);
        }
    }

    @Override
    public void onDisable() {

    }
    public void loadCommand(){
        getCommand("aethertotems").setExecutor(new MainCommand());
    }
    public void loadConfigs(){
        ConfigManager.load("i18n.yml");
        ConfigManager.load("config.yml");
        ConfigManager.load("messages.yml");
        ConfigManager.load("totems.yml");
    }
    public void loadSupport(){
        if (NBTEditor.getMinecraftVersion().greaterThanOrEqualTo(NBTEditor.MinecraftVersion.v1_9)){
            Bukkit.getPluginManager().registerEvents(new ResurrectFix(), this);
        }
        if (NBTEditor.getMinecraftVersion().greaterThanOrEqualTo(NBTEditor.MinecraftVersion.v1_13)){
            this.support = new New();
        }else {
            this.support = new Legacy();
        }
    }
    public void loadEvents(){
        Bukkit.getPluginManager().registerEvents(new OtherFixes(), this);
        Bukkit.getPluginManager().registerEvents(new Inspect(), this);
    }
    public Support getSupport() {
        return support;
    }
}
