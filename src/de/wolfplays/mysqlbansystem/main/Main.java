package de.wolfplays.mysqlbansystem.main;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import de.wolfplays.mysqlbansystem.cmd.Cmd_ban;
import de.wolfplays.mysqlbansystem.cmd.Cmd_warn;
import de.wolfplays.mysqlbansystem.listener.Listener_PlayerJoinEvent;
import de.wolfplays.mysqlbansystem.listener.Listener_PlayerLoginEvent;
import de.wolfplays.mysqlbansystem.util.FileManager;
import de.wolfplays.mysqlbansystem.util.MySQL;

/**
 * Created by WolfPlaysDE
 * On 30.03.2015 at 02:13:28
 */
public class Main extends JavaPlugin {

	public String prefix;
	
	public static Main Instance;
	
	@Override
	public void onLoad() {
		Main.Instance = this;
	}
	
	@Override
	public void onEnable() {
		FileManager.setupConfigs();
		
		MySQL.connect();
		
		this.getCommand("permban").setExecutor(new Cmd_ban());
		this.getCommand("tempban").setExecutor(new Cmd_ban());
		this.getCommand("unban").setExecutor(new Cmd_ban());
		this.getCommand("checkban").setExecutor(new Cmd_ban());
		
		this.getCommand("addwarn").setExecutor(new Cmd_warn());
		this.getCommand("removewarn").setExecutor(new Cmd_warn());
		this.getCommand("checkwarn").setExecutor(new Cmd_warn());
		
		this.getServer().getPluginManager().registerEvents(new Listener_PlayerLoginEvent(), this);
		this.getServer().getPluginManager().registerEvents(new Listener_PlayerJoinEvent(), this);
		
		this.getServer().getConsoleSender().sendMessage(prefix + "Config Datei: " + (FileManager.getConfigFile().exists() ? "§2erfolgreich geladen!" : "§4nicht gefunden!"));
		this.getServer().getConsoleSender().sendMessage(prefix + "MySQL Datei: " + (FileManager.getMySQLFile().exists() ? "§2erfolgreich geladen!" : "§4nicht gefunden!"));
		
		this.getServer().getConsoleSender().sendMessage(prefix + " MySQL Verbindungsaufgebau: " + (MySQL.con == null ? "§4ist fehlgeschlagen!" : "§2war erfolgreich!"));
		if (MySQL.con == null) {
	      Plugin plugin = getServer().getPluginManager().getPlugin("MySQLBanSystem");
	      getServer().getPluginManager().disablePlugin(plugin);
	      this.getServer().getConsoleSender().sendMessage(prefix + " §4§lDAS PLUGIN WURDE DEAKTIVIRT, WEIL DIE MYSQL VERBINDUNG FEHLGESCHLAGEN IST!");
	    }
	}
		
	@Override
	public void onDisable() {
		MySQL.diconnect();
	}

	public static Main getInstance() {
		return Instance;
	}

}
