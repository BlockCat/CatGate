package me.blockcat.loader;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.GZIPInputStream;

import me.blockcat.catgame.CatArena;
import me.blockcat.loader.blocks.Blocks;
import me.blockcat.loader.blocks.BlocksChest;
import me.blockcat.loader.blocks.BlocksSign;

import org.bukkit.Material;
import org.bukkit.World;

public class prRegion {

	private String name;

	List<Blocks> blocks = new ArrayList<Blocks>();
	private World world;
	private CatArena catArena;
	ExecutorService exec = Executors.newCachedThreadPool();

	public prRegion() {

	}

	public void load(DataInputStream in, String name, World w) {
		this.name = name;
		try {
			world = w;
			try {
				double x1 = in.readDouble();
				double y1 = in.readDouble();
				double z1 = in.readDouble();
				catArena.setLocB(world, x1, y1, z1);

				double x2 = in.readDouble();
				double y2 = in.readDouble();
				double z2 = in.readDouble();
				catArena.setLocG(world, x2, y2, z2);

				while (true) {

					int d = in.readInt();
					if (d == Material.CHEST.getId()) {
						blocks.add(new BlocksChest(in, world));
					} else if (d == Material.SIGN.getId()
							|| d == Material.SIGN_POST.getId()) {
						blocks.add(new BlocksSign(in, world));

					} else {
						blocks.add(new Blocks(in, world));
					}
				}
			} catch (EOFException d) {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadFromFile(String file, World world) {
		File f = new File("plugins/CatGate/arenas/" + file + ".fr");
		if (!f.getParentFile().exists()) {
			f.getParentFile().mkdirs();
		}
		if (!f.exists()) {
			return;
		}
		try {
			GZIPInputStream gzipi = new GZIPInputStream(new FileInputStream(f));
			DataInputStream in = new DataInputStream(gzipi);

			this.load(in, f.getName().replace(".fr", ""), world);
			in.close();
			gzipi.close();
		} catch (IOException e) {

		}
	}

	public void save(DataOutputStream out) {
		try {
			for (Blocks b : blocks) {
				b.save(out);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getWorld() {
		return world.getName();
	}

	public void place(String string, World world, CatArena catArena) {
		this.catArena = catArena;
		Replacer replace = new Replacer(string, world);
		exec.execute(replace);
	}

	public class Replacer extends Thread {

		String fileName;
		World world;

		public Replacer(String string, World world) {
			this.fileName = string;
			this.world = world;
		}

		@Override
		public void run() {
			loadFromFile(this.fileName, this.world);

			for (Blocks bl : blocks) {
				bl.set();
			}
		}

	}

}
