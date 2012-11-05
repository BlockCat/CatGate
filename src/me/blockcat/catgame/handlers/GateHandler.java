package me.blockcat.catgame.handlers;

import java.util.ArrayList;
import java.util.List;

import me.blockcat.catgame.CGate;
import me.blockcat.catgame.CatGame;
import me.blockcat.catgame.ListenerUtils.CatEvent;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;

public class GateHandler extends Handler {

	private CatGame plugin;
	private static List<Sign> signs = new ArrayList<Sign>();

	public GateHandler(CatGame plugin) {
		this.plugin = plugin;
	}

	@CatEvent(event=SignChangeEvent.class)
	public void onSignChange(SignChangeEvent event) {
		if (event.getLine(0).equalsIgnoreCase("[cGate]")) {
			if (CatGame.hasPerms(event.getPlayer(), "catgame.create.sign")) {
				event.getPlayer().sendMessage(ChatColor.AQUA + "Sign created!");
			} else {
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.RED + "You do not have these permissions.");
			}
		}
	}

	public static void addSign(Sign sign) {
		if (signs.contains(sign)) return;

		signs.add(sign);
	}

	public static List<Sign> getSigns() {
		return signs;
	}


	@CatEvent(event = BlockRedstoneEvent.class)
	public void onBlockRedstoneEvent(BlockRedstoneEvent event) {
		if (plugin.catpvp.playing) 
		{

			Block block = event.getBlock();

			int startX = block.getX() - 1;
			int startY = block.getY() - 1;
			int startZ = block.getZ() - 1;
			int endX = block.getX() + 1;
			int endY = block.getY() + 1;
			int endZ = block.getZ() + 1;
			for (int x = startX; x <= endX; x++)
				for (int y = startY; y <= endY; y++)
					for (int z = startZ; z <= endZ; z++) {
						Block b = block.getWorld().getBlockAt(x, y, z);
						if (b.getTypeId() == 63 || b.getTypeId() == 68) {
							Sign sign = (Sign) b.getState();
							if (sign.getLine(0).equalsIgnoreCase("[cGate]") && sign.getLine(1).equalsIgnoreCase("controller")) {
								try {

									if (b.isBlockPowered()) {
										//0 == Gold
										//1 == iron
										for (Sign s : getSigns()) {
											//is freq equal?
											if (s.getLine(1).equalsIgnoreCase(sign.getLine(2))) {
												//0 goes down, 1 goes up.
												new CGate(s).up(Integer.parseInt(s.getLine(2)));
											}
										}

									} else {
										for (Sign s : getSigns()) {
											if (s.getLine(1).equalsIgnoreCase(sign.getLine(2))) {
												//0 goes up, 1 goes down.
												new CGate(s).down(Integer.parseInt(s.getLine(2)));
											}
										}
									}
								}catch (Exception e) {}
							}
						}
					}
		}
	}

}
