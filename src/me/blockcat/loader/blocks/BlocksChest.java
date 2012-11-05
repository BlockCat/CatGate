package me.blockcat.loader.blocks;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

public class BlocksChest extends Blocks{

	private List<ItemStack> content = new ArrayList<ItemStack>();

	public BlocksChest(World world, int x, int y, int z, int id, byte data) {
		super(world, x, y, z, id, data);
	}

	public BlocksChest(World world, int x, int y, int z, int id, byte data, BlockState state) {
		super(world,x, y, z, id, data, state);
		for (int i = 0; i < ((Chest) state).getBlockInventory().getSize(); i++) {
			ItemStack item = ((Chest) state).getBlockInventory().getItem(i);
			if (item != null) {
				this.content.add(new ItemStack(item.getTypeId(),item.getAmount()));
			}
		}
	}

	public BlocksChest(DataInputStream in, World world) throws IOException {
		super(in, world);
		int index = in.readInt();
		for (int x = 0; x < index; x++) {
			int id = in.readInt();
			int a = in.readInt();
			short damage = in.readShort();
			byte data = in.readByte();
			this.content.add(new ItemStack(id, a, damage, data));
		}
	}

	public void save(DataOutputStream out) throws IOException {
		super.save(out);
		out.writeInt(content.size());
		for (ItemStack c : content) {
			out.writeInt(c.getTypeId());
			out.writeInt(c.getAmount());
			out.writeShort(c.getDurability());
			out.writeByte(c.getData().getData());
		}
	}

	public void set() {
		Block b = world.getBlockAt(x, y, z);
		b.setTypeIdAndData(id, data, true);
		Chest chestNew = (Chest) b.getState();
		chestNew.getBlockInventory().clear();
		for (ItemStack item : content) {
			chestNew.getBlockInventory().addItem(item);
		}
	}
}
