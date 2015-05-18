package de.wolfplays.mysqlbansystem.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.wolfplays.mysqlbansystem.util.BanManager;

/**
 * Created by WolfPlaysDE
 * On 30.03.2015 at 06:32:58
 */
public class Listener_PlayerJoinEvent implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		if(BanManager.isBanned(p.getUniqueId().toString())) {
			BanManager.unban(p.getUniqueId().toString());
		}
	}
	
}
