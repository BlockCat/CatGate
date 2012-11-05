package me.blockcat.catgame;

import me.blockcat.catgame.ListenerUtils.LHandler;
import me.blockcat.catgame.handlers.PVPHandler;
import me.blockcat.catgame.handlers.GateHandler;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.teleport.TeleportFix;

public class CatGame extends JavaPlugin {

	public PVPHandler catpvp;
	public CatCommands commands;
	public CatConfig config;
	public static Permission permission = null;
	public GateHandler gate;
	public LHandler lhandler;

	@Override
	public void onEnable() {
		this.setupPermissions();
		lhandler = new LHandler(this);
		gate = new GateHandler(this);
		config = new CatConfig(this);
		catpvp = new PVPHandler(this);
		commands = new CatCommands(this);
		config.load();

		// add classes to lhandler
		lhandler.addClass(gate);
		lhandler.addClass(catpvp);

		this.getCommand("cg").setExecutor(commands);
		this.getServer().getPluginManager()
				.registerEvents(new CatListener(this, lhandler), this);
		this.getServer().getPluginManager()
				.registerEvents(new TeleportFix(this), this);

	}

	@Override
	public void onDisable() {
		catpvp.end();
	}

	private boolean setupPermissions() {
		RegisteredServiceProvider<Permission> permissionProvider = getServer()
				.getServicesManager().getRegistration(
						net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) {
			permission = permissionProvider.getProvider();
		}
		return (permission != null);
	}

	public static boolean hasPerms(Player player, String node) {
		if (player.isOp()) {
			return true;
		} else {
			return permission.has(player, node);
		}
	}
}
