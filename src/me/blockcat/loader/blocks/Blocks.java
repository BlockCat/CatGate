package me.blockcat.loader.blocks;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import me.blockcat.catgame.handlers.GateHandler;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

public class Blocks {
	
	protected int x,y,z;
	protected int id;
	protected byte data;
	protected World world;
	
	public Blocks(World world, int x, int y, int z, int id, byte data) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.id = id;
		this.data = data;
		this.world = world;
	}
	
	public Location getLocation() {
		return new Location(world, x,y,z);
	}
	
	public Blocks(World world, int x, int y, int z, int id, byte data, BlockState state) {
		this(world, x, y, z, id, data);
	}
	
	public Blocks(DataInputStream in, World world) throws IOException {
		this.x = in.readInt();
		this.y = in.readInt();
		this.z = in.readInt();
		this.id = in.readInt();
		this.data = in.readByte();
		this.world = world;
	}

	public void save(DataOutputStream out) throws IOException {
		out.writeInt(id);
		out.writeInt(x);
		out.writeInt(y);
		out.writeInt(z);
		out.writeInt(id);
		out.writeByte(data);
	}

	public int test() {
		return id;
	}

	public void set() {
		
		Block b = world.getBlockAt(x, y, z);
		b.setTypeIdAndData(id, data, true);
	}

}
