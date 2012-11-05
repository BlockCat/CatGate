package me.blockcat.catgame;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class CatConfig {

	private CatGame plugin;

	private static String world;
	private static int lives;
	private static int minimum;

	private final FileConfiguration f;

	public CatConfig(CatGame plugin) {
		this.plugin = plugin;
		f = plugin.getConfig();
	}

	public void load() {

		if (!f.contains("World")) {
			f.set("World", "arenaWorld");
		}
		if (!f.contains("lives")) {
			f.set("lives", 3);
		}
		if (!f.contains("minimum-players")) {
			f.set("minimum-players", 3);
		}
		
		world = f.getString("World");
		
		lives = f.getInt("lives");
		minimum = f.getInt("minimum-players");
		


		plugin.saveConfig();
	}
	
	public static int getLives() {
		return lives;
	}

	public World getWorld() {
		return Bukkit.getWorld(world);
	}
	public static String getWorldName() {
		return world;
	}


	public static int getMinimum() {
		return minimum;
	}

}
