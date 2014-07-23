package diefriiks.RegionInv;

import java.sql.ResultSet;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.mewin.WGRegionEvents.events.RegionEnterEvent;
import com.mewin.WGRegionEvents.events.RegionLeftEvent;
import com.sk89q.worldguard.LocalPlayer;

public class EventListener implements Listener {
	
	private Executor exec;
	private RegionInv plugin;

	public EventListener(RegionInv regioninv, Executor executor) {
		this.exec = executor;
		this.plugin = regioninv;
		
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	public boolean checkregion(String regionname){
		
		if(plugin.usemysql){
			ResultSet r = exec.getmysql("SELECT toggle FROM regions WHERE regionname = '" + regionname + "';");
			try{
			r.next();
					return r.getBoolean("toggle");
			}
			catch(Exception e){
	        	if(plugin.debug){
	        		e.printStackTrace();
	        	}
			}
		}
		else {
			return plugin.config.getBoolean("regions." + regionname);
		}
		return false;
	}
	
	@EventHandler
	public void onPlayerEnter(RegionEnterEvent e) {
		
		Player p = e.getPlayer();
		if ( !(plugin.config.getBoolean("bypass." + p.getName())== true) && p.hasPermission("regioninv.bypass") || p.isOp()) {
			if(!e.getRegion().isMember((LocalPlayer) p)) {
				if (checkregion(e.getRegion().getId())) {
					if (plugin.usemysql) {
						exec.toggleinv(true, p.getName(), true);
					}
					else {
						exec.toggleinv(false, p.getName(), true);
					}

				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerEnter(RegionLeftEvent e) {
		
		Player p = e.getPlayer();
		if ( !(plugin.config.getBoolean("bypass." + p.getName())== true) && p.hasPermission("regioninv.bypass") || p.isOp()) {
			if (plugin.usemysql) {
				exec.toggleinv(true, p.getName(), false);
			}
			else {
				exec.toggleinv(false, p.getName(), false);
			}
		}
	}
	
}