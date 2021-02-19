package com.kardasland.aethertotems.totem;

import com.kardasland.aethertotems.utils.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Totem {
    private String totemtype;
    private List<PotionEffectType> effects;
    private Map<PotionEffectType, Integer> effectTypeMap = new HashMap<>();
    private Integer fuel_cap;
    private Integer fuel_duration;
    private Integer take_fuel_amount;

    public Totem(String totemtype){
        this.totemtype = totemtype;
        FileConfiguration cf = ConfigManager.get("totems.yml");
        this.fuel_cap = cf.getInt("Totems."+totemtype+".fuel.fuel-cap");
        this.take_fuel_amount = cf.getInt("Totems."+totemtype+".fuel.amount");
        this.fuel_duration = cf.getInt("Totems."+totemtype+".fuel.duration");
        for (String key : cf.getStringList("Totems."+totemtype+".effects-when-active")){
            String[] effect = key.split(",", 2);
            try {
                this.effectTypeMap.put(PotionEffectType.getByName(effect[0]), Integer.parseInt(effect[1]));
            }catch (IllegalArgumentException ex){
                this.effectTypeMap.put(PotionEffectType.getByName(effect[0]), 0);
            }
            //this.effects.add(PotionEffectType.getByName(key));
        }
    }

    public Integer getFuel_cap() {
        return fuel_cap;
    }

    public Map<PotionEffectType, Integer> getEffectTypeMap() {
        return effectTypeMap;
    }

    public Integer getFuel_duration() {
        return fuel_duration;
    }

    public String getTotemtype() {
        return totemtype;
    }

    public Integer getTake_fuel_amount() {
        return take_fuel_amount;
    }
    public TotemItem toItem(){
        return new TotemItem(this);
    }
}
