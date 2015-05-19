package de.wolfplays.mysqlbansystem.util;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import de.wolfplays.mysqlbansystem.main.Main;

/**
 * Created by WolfPlaysDE
 * On 30.03.2015 at 02:14:26
 */
public class FileManager {
	
	// Config File
	public static File getConfigFile() {
		return new File(Main.getInstance().getDataFolder(), "config.yml");
	}
	public static FileConfiguration getConfigFileConfiguration() {
		return YamlConfiguration.loadConfiguration(getConfigFile());
	}
	
	// MySQL File
	public static File getMySQLFile() {
		return new File(Main.getInstance().getDataFolder(), "mysql.yml");
	}
	public static FileConfiguration getMySQLFileConfiguration() {
		return YamlConfiguration.loadConfiguration(getMySQLFile());
	}
	
	// Config File setup
	public static void setDefaultConfig() {
		FileConfiguration cfg = getConfigFileConfiguration();
		cfg.options().copyDefaults(true);
		cfg.addDefault("prefix", "&8[&6MySQLBanSystem&8]");
		cfg.addDefault("maxwarns", "8");
//		cfg.addDefault("warnpermban", "true");
//		cfg.addDefault("warnbantime", "4");
//		cfg.addDefault("warnbantimeunit", "week");
		try {
			cfg.save(getConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void readConfig() {
		FileConfiguration cfg = getConfigFileConfiguration();
		Main.getInstance().prefix = ChatColor.translateAlternateColorCodes('&', cfg.getString("prefix") + "§r ");
		WarnManager.maxwarns = Integer.valueOf(cfg.getString("maxwarns"));
//		WarnManager.warnPermBan = cfg.getBoolean("warnpermban");
//		WarnManager.warnBanTime = Integer.valueOf(cfg.getString("warnbantime"));
//		WarnManager.warnBanTimeUnit = cfg.getString("warnbantimeunit");
	}
	
	//MySQL File setup
	public static void setDefaultMySQL() {
		FileConfiguration cfg = getMySQLFileConfiguration();
		cfg.options().copyDefaults(true);
		cfg.addDefault("username", "root");
		cfg.addDefault("passwort", "");
		cfg.addDefault("host", "localhost");
		cfg.addDefault("port", "3306");
		cfg.addDefault("database",  "minecraft_bansystem");
		try {
			cfg.save(getMySQLFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void readMySQL() {
		FileConfiguration cfg = getMySQLFileConfiguration();
		MySQL.username = cfg.getString("username");
		MySQL.passwort = cfg.getString("passwort");
		MySQL.host = cfg.getString("host");
		MySQL.port = cfg.getString("port");
		MySQL.database = cfg.getString("database");
	}
	
	// Setup all Configs
	public static void setupConfigs() {
		FileManager.setDefaultConfig();
		FileManager.readConfig();
		FileManager.setDefaultMySQL();
		FileManager.readMySQL();
	}
	
}
