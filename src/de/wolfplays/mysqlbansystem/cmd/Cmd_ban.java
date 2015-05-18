package de.wolfplays.mysqlbansystem.cmd;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.wolfplays.mysqlbansystem.main.Main;
import de.wolfplays.mysqlbansystem.util.BanManager;
import de.wolfplays.mysqlbansystem.util.BanUnit;
import de.wolfplays.mysqlbansystem.util.UUIDFetcher;

/**
 * Created by WolfPlaysDE
 * On 30.03.2015 at 04:49:34
 */
public class Cmd_ban implements CommandExecutor {

	private String getUUID(String playername) {
		try {
			return UUIDFetcher.getUUIDOf(playername).toString();
		} catch (Exception e) {}
		return null;
	}
	
	private String prefix = Main.getInstance().prefix;
	
	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		
		// Player permban
		if(cmd.getName().equalsIgnoreCase("permban")) {
			if(!cs.isOp()) return true;
			if(args.length >= 2) {
				String playername = args[0];
				if(getUUID(playername) == null) {
					cs.sendMessage(prefix + "§7Der Spieler §e" + playername + " §7existiert nicht!");
					return true;
				}
				if(BanManager.isBanned(getUUID(playername))) {
					cs.sendMessage(prefix + "§cDieser Spieler ist bereits vom Server gebannt!");
					return true;
				}
				String reason = "";
				for(int i = 1; i < args.length; i++) {
					reason += args[i] + " ";
				}
				BanManager.ban(args[0], getUUID(playername), reason, -1);
				cs.sendMessage(prefix + "§7Du hast §e" + playername + " §4PERMANET §7von dem Server gebant!");
				return true;
			}
			cs.sendMessage(prefix + "§c/permban <Spieler> <Grund>");
			return true;
		}
		
		// Player tempban
		if(cmd.getName().equalsIgnoreCase("tempban")) {
			if(!cs.isOp()) return true;
			if(args.length >= 4) {
				String playername = args[0];
				if(getUUID(playername) == null) {
					cs.sendMessage(prefix + "§7Der Spieler §e" + playername + " §7existiert nicht!");
					return true;
				}
				if(BanManager.isBanned(getUUID(playername))) {
					cs.sendMessage(prefix + "§cDieser Spieler ist bereits vom Server gebannt!");
					return true;
				}
				int value;
				try {
					value = Integer.valueOf(args[1]);
				}catch(NumberFormatException e) {
					cs.sendMessage(prefix + "§c<Zahlenwert> muss eine Zahl sein!");
					return true;
				}
				if(value >= 500) {
					cs.sendMessage(prefix + "§cDie Zahl mus unter 500 liegen!");
					return true;
				}
				String unitString = args[2];
				String reason = "";
				for(int i = 3; i < args.length; i++) {
					reason += args[i] + " ";
				}
				List<String> unitsList = BanUnit.getUnitsAsString();
				if(unitsList.contains(unitString.toLowerCase())) {
					BanUnit unit = BanUnit.getUnit(unitString);
					long seconds = value * unit.getToSecond();
					BanManager.ban(playername, getUUID(playername), reason, seconds);
					cs.sendMessage("§7Du hast §e" + playername + " §7für §c" + value + unit.getName() + " §7vom Server gebannt!");
					return true;
				}
				cs.sendMessage(prefix + "§cDiese <Einheit> existiert nicht!(sec/min/hour/day/week)");
				return true;
			}
			cs.sendMessage(prefix + "§c/tempban <Spieler> <Zahlenwert> <Einheit(sec/min/hour/day/week)> <Grund>");
			return true;
		}
		
		// Unban Player
		if(cmd.getName().equalsIgnoreCase("unban")) {
			if(!cs.isOp()) return true;
			if(args.length == 1) {
				String playername = args[0];
				if(getUUID(playername) == null) {
					cs.sendMessage(prefix + "§7Der Spieler §e" + playername + " §7existiert nicht!");
					return true;
				}
				if(!(BanManager.isBanned(getUUID(playername)))) {
					cs.sendMessage(prefix + "§cDieser Spieler ist nicht gebannt!");
					return true;
				}
				BanManager.unban(getUUID(playername));
				cs.sendMessage(prefix + "§7Du hast §e" + playername + " §7entbannt!");
				return true;
			}
			cs.sendMessage(prefix + "§c/unban <Spieler>");
			return true;
		}
		
		// Check Player banned
		if(cmd.getName().equalsIgnoreCase("checkban")) {
			if(!cs.isOp()) return true;
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("list")) {
					if(BanManager.getBannedPlayers().isEmpty()) {
						cs.sendMessage(prefix + "§cMomentan sind keine Spieler vom Server gebannt!");
						return true;
					}
					cs.sendMessage("§7---------[§6§lBan-Liste§r§7]---------");
					for(String allBanned : BanManager.getBannedPlayers()) {
						cs.sendMessage(prefix + "§e" + allBanned + " §7(Grund: §r" + BanManager.getReason(getUUID(allBanned)) + "§7)");
					}
					return true;
				}
				String playername = args[0];
				if(getUUID(playername) == null) {
					cs.sendMessage(prefix + "§7Der Spieler §e" + playername + " §7existiert nicht!");
					return true;
				}
				cs.sendMessage(prefix + "§7---------[§6§lBan-Infos§r§7]---------");
				cs.sendMessage(prefix + "§eName: §r" + playername);
				cs.sendMessage(prefix + "§eGebannt: §r" + (BanManager.isBanned(getUUID(playername)) ? "§aJa!" : "§aNein!"));
				if(!BanManager.isBanned(getUUID(playername))) return true;
				cs.sendMessage(prefix + "§aGrund: §r" + BanManager.getReason(getUUID(playername)));
				cs.sendMessage(prefix + "§aVerbleiben Zeit: §r" + BanManager.getRemaningTime(getUUID(playername)));
				return true;
			}
			cs.sendMessage(prefix + "§c/check <list/Spieler>");
			return true;
		}
		
		return false;
	}

}
