package me.blockcat.loader.blocks;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import me.blockcat.catgame.handlers.GateHandler;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;

public class BlocksSign extends Blocks {

	String[] lines = new String[4];

	public BlocksSign(World world, int x, int y, int z, int id, byte data) {
		super(world, x, y, z, id, data);
	}

	public BlocksSign(World world, int x, int y, int z, int id, byte data,
			BlockState state) {
		super(world, x, y, z, id, data, state);
		Sign sign = (Sign) state;
		lines = sign.getLines();
	}

	public BlocksSign(DataInputStream in, World world) throws IOException {
		super(in, world);
		for (int i = 0; i < 4; i++) {
			lines[i] = in.readUTF();
		}
	}

	public void save(DataOutputStream out) throws IOException {
		super.save(out);
		for (int i = 0; i < 4; i++) {
			if (lines[i].isEmpty()) {
				out.writeUTF("");
			} else {
				out.writeUTF(lines[i]);
			}
		}
	}

	@Override
	public void set() {
		Block b = world.getBlockAt(x, y, z);
		b.setTypeIdAndData(id, data, true);
		Sign sign = (Sign) b.getState();
		sign.setLine(0, lines[0]);
		sign.setLine(1, lines[1]);
		sign.setLine(2, lines[2]);
		sign.setLine(3, lines[3]);
		sign.update(true);
		GateHandler.addSign(sign);
	}
}
