package me.Zindev.mechanics.ArmorEffects;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.Zindev.mechanics.ArmorEffects.commands.CMD_ArmorEffects;

public class MainArmor extends JavaPlugin {

	private ArmorFactory armorFactory;
	private static MainArmor jp;

	@Override
	public void onEnable() {
		getConfig().options().copyDefaults(true);
		if(!new File(getDataFolder(),"/config.yml").exists())
			saveConfig();
		
		getCommand("armoreffects").setExecutor(new CMD_ArmorEffects());
		jp = this;
		
		loadConfig();
		armorFactory = new ArmorFactory(this);
		armorFactory.initialize();
		getLogger().info("Loaded successfully.");
	}

	public static void loadConfig() {
		jp.reloadConfig();
	}

	public static void reload(){
		factoryA().uninitialize();
		loadConfig();
		factoryA().initialize();
	}
	@Override
	public void onDisable() {
		armorFactory.uninitialize();
		getLogger().info("Unloaded successfully.");
	}

	public static ArmorFactory factoryA() {
		return jp.armorFactory;
	}

	public static FileConfiguration conf() {
		return jp.getConfig();
	}
}
