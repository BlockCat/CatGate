package me.blockcat.catgame;

import java.io.File;
import java.util.Random;

import me.blockcat.loader.prRegion;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;

public class CatArena {


	private World world;
	private Location locG;
	private Location locB;
	private CatGame plugin;
	private String name;

	public CatArena(CatGame plugin) {
		this.plugin = plugin;
	}

	private String chooseArena() {
		String arena = "arena";

		File f = new File("plugins/CatGate/arenas/");
		if (!f.exists()) {
			f.mkdirs();
		}
		if (f.listFiles().length == 0) {
			return "";
		}
		
		Random random = new Random();
		int index = random.nextInt(f.listFiles().length);
		try {
		arena = f.listFiles()[index].getName().replace(".fr", "");
		} catch (Exception e) {
			arena = "arena";
		}

		return arena;
	}

	public World createArena() {
		WorldCreator wc = new WorldCreator(CatConfig.getWorldName());
		wc.environment(Environment.NORMAL);
		wc.type(WorldType.FLAT);
		world = wc.createWorld();

		prRegion pr = new prRegion();
		try {
			String arena = chooseArena();

			if (arena.equalsIgnoreCase("")) {
				return null;
			}
			pr.place(arena, world, this);
			this.name = arena;
		} catch (Exception e) {
			System.out.println("Failed");
		}
		removeMobs();
		return world;
	}
	
	public String getName() {
		return name;
	}

	private void removeMobs() {
		for(Entity ent : world.getEntities()) {
			if (ent instanceof Monster || ent instanceof Animals) {
				ent.remove();
			}
		}
	}

	public Location getLocG() {
		return locG;
	}
	public Location getLocB() {
		return locB;
	}

	public void setLocG(World world, double x2, double y2, double z2) {
		locG = new Location(world, x2, y2, z2);
	}
	public void setLocB(World world, double x1, double y1, double z1) {
		locB = new Location(world, x1, y1, z1);
	}

}
