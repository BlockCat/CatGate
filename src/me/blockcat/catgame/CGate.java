package me.blockcat.catgame;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public class CGate {
	
	private Block starter;
	private List<Block> gateParts = new ArrayList<Block>();

	public CGate(Sign s) {
		if (s.getTypeId() == 0) return;
		
		starter = s.getWorld().getBlockAt(s.getX(), s.getY() + 1, s.getZ());
		registerBlock(starter);
	}

	private void registerBlock(Block block) {
		if (gateParts.contains(block)) return;
		
		World world = block.getWorld();
		gateParts.add(block);
		
		int startX = block.getX()-1;
		int startZ = block.getZ()-1;
		int endX = block.getX()+1;
		int endZ = block.getZ()+1;
		for(int x = startX; x <= endX; x++)
				for(int z = startZ; z <= endZ; z++) {
					Block b = world.getBlockAt(x,block.getY(),z);
					if (b.getTypeId() == block.getTypeId()) {
						registerBlock(b);
					}
				}
		
	}

	public void up(int i) {
		if (i == 0) {
			this.down(1);
			return;		
		}
		for (Block block : gateParts) {
			World world = block.getWorld();
			world.getBlockAt(block.getX(), block.getY() + 1, block.getZ()).setTypeIdAndData(block.getTypeId(), block.getData(), true);
			world.getBlockAt(block.getX(), block.getY() + 2, block.getZ()).setTypeIdAndData(block.getTypeId(), block.getData(), true);
		}
	}

	public void down(int i) {
		if (i == 0) {
			this.up(1);
			return;		
		}
		for (Block block : gateParts) {
			World world = block.getWorld();
			world.getBlockAt(block.getX(), block.getY() + 1, block.getZ()).setTypeId(0);
			world.getBlockAt(block.getX(), block.getY() + 2, block.getZ()).setTypeId(0);
		}
	}

}
