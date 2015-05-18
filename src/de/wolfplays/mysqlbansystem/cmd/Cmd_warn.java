package de.wolfplays.mysqlbansystem.cmd;

import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.wolfplays.mysqlbansystem.main.Main;
import de.wolfplays.mysqlbansystem.util.BanManager;
import de.wolfplays.mysqlbansystem.util.UUIDFetcher;
import de.wolfplays.mysqlbansystem.util.WarnManager;

/**
 * Created by WolfPlaysDE
 * On 03.04.2015 at 03:11:51
 */
public class Cmd_warn implements CommandExecutor {

	private UUID getUUID(String playername) {
		try {
			return UUIDFetcher.getUUIDOf(playername);
		} catch (Exception e) {}
		return null;
	}
	
	private String prefix = Main.getInstance().prefix;

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		
		Player p = (Player) cs;
		
		// Add a warn
		if(cmd.getName().equalsIgnoreCase("addwarn")) {
			if(!cs.isOp()) return true;
			if(args.length >= 2) {
				String playername = args[0];
				if(getUUID(playername) == null) {
					cs.sendMessage(prefix + "§7Der Spieler §e" + playername + " §7existiert nicht!");
					return true;
				}
				if(WarnManager.getWarns(getUUID(playername).toString()) == WarnManager.maxwarns) {
					cs.sendMessage(prefix + "§7Der Spieler §e" + playername + " §7hat schon die maximale Anzahl an Warns!");
					return true;
				}
				String reason = "";
				for(int i = 1; i < args.length; i++) {
					reason += args[i] + " ";
				}
				if(!WarnManager.isInTabel(getUUID(playername).toString())) {
					OfflinePlayer op = Main.getInstance().getServer().getOfflinePlayer(getUUID(playername));
					WarnManager.addPlayer(op.getName(), op.getUniqueId().toString());
				}
				WarnManager.addWarn(getUUID(playername).toString(), 1);
				Main.getInstance().getServer().broadcastMessage("§7---------[§6§lWarn-Info§r§7]---------");
				Main.getInstance().getServer().broadcastMessage("§e" + playername + " §7hat einen neuen Warn bekommen!");
				Main.getInstance().getServer().broadcastMessage("§7Warns: §e" + WarnManager.getWarns(getUUID(playername).toString()) + "/" + WarnManager.maxwarns);
				Main.getInstance().getServer().broadcastMessage("§7Grund: §e" + reason);
				Main.getInstance().getServer().broadcastMessage("§7---------[§6§lWarn-Info§r§7]---------");
				if(WarnManager.getWarns(getUUID(playername).toString()) == WarnManager.maxwarns) {
					BanManager.ban(args[0], getUUID(playername).toString(), "Zu Viele Warns!", -1);
					WarnManager.deletePlayer(getUUID(playername).toString());
				}
				return true;
			}
			cs.sendMessage(prefix + "§c/addwarn <Spieler> <Grund>");
			return true;
		}	
		
		// Remove a warn
		if(cmd.getName().equalsIgnoreCase("removewarn")) {
			if(!cs.isOp()) return true;
				if(args.length == 1) {
					String playername = args[0];
					if(getUUID(playername) == null) {
						cs.sendMessage(prefix + "§7Der Spieler §e" + playername + " §7existiert nicht!");
						return true;
					}
					if(WarnManager.getWarns(getUUID(playername).toString()) ==  0 ) {
						cs.sendMessage(prefix + "§7Der Spieler §e" + playername + " §7hat schon 0 Warns!");
						return true;
					}
					WarnManager.deleteWarns(getUUID(playername).toString(), 1);
					cs.sendMessage("§7Du hast §e" + playername + " §71 Warn entfernt!");
					return true;
				}
				cs.sendMessage(prefix + "§c/removewarn <Spieler>");
				return true;
			}
		
		// Check Warns from a Player
		if(cmd.getName().equalsIgnoreCase("checkwarn")) {
			if(cs.isOp()) {
				if(args.length == 1) {
					String playername = args[0];
					if(getUUID(playername) == null) {
						cs.sendMessage(prefix + "§7Der Spieler §e" + playername + " §7existiert nicht!");
						return true;
					}
					cs.sendMessage(prefix + "§7---------[§6§lWarn-Infos§r§7]---------");
					cs.sendMessage(prefix + "§7Name: §e" + playername);
					cs.sendMessage(prefix + "§7Warns: §e" + WarnManager.getWarns(getUUID(playername).toString()));
					return true;
				}
				cs.sendMessage(prefix + "§c/checkwarn <Spieler>");
				return true;
			} else {
				if(args.length == 0) {
					cs.sendMessage(prefix + "§7---------[§6§lWarn-Infos§r§7]---------");
					cs.sendMessage(prefix + "§7Name: §e" + p.getName());
					cs.sendMessage(prefix + "§7Warns: §e" + WarnManager.getWarns(p.getUniqueId().toString()));
					return true;
				}
				cs.sendMessage(prefix + "§c/checkwarn");
				return true;
			}
		}
		
		return true;
	}
	
}
