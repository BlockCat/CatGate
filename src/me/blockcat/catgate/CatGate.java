package me.blockcat.catgate;

import org.bukkit.plugin.java.JavaPlugin;

public class CatGate extends JavaPlugin{
	
	public CatPvP catpvp;
	public CatCommands commands;
	
	@Override
	public void onEnable() {
		catpvp = new CatPvP(this);
		commands = new CatCommands(this);
		this.getCommand("start").setExecutor(commands);
		this.getServer().getPluginManager().registerEvents(new CatListener(this), this);
	}
	
	@Override
	public void onDisable() {
		catpvp.end();
	}

}
