package me.blockcat.catgate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CatPvP {

	private CatGate plugin;

	public HashMap<Player, Integer> lives = new HashMap<Player, Integer>();	
	public List<Player> teamGold = new ArrayList<Player>();
	public List<Player> teamBlue = new ArrayList<Player>();
	public List<Player> dead = new ArrayList<Player>();
	private Location tGold;
	private Location tBlue;
	public boolean playing = false;


	public CatPvP(CatGate plugin) {
		this.plugin = plugin;
		spawnLocation();
	}

	private void spawnLocation() {
		tGold = new Location(Bukkit.getWorld("world"), 926,30,-349);
		tBlue =	new Location(Bukkit.getWorld("world"), 925, 30, -234, 180 , 0);
	}

	public void start() {
		playing = true;
		int i = 1;
		for (Player player : Bukkit.getOnlinePlayers()) {
			
			player.setGameMode(GameMode.ADVENTURE);
			player.setHealth(20);
			player.setFoodLevel(20);
			
			
			lives.put(player, 3);
			player.getInventory().clear();
			if (i == 1) {
				i = 2;
				player.sendMessage(ChatColor.GOLD + "You are in team: Gold");
				player.getInventory().setHelmet(new ItemStack(Material.WOOL.getId(), 1, (short) 0, (byte) 1));

				teamGold.add(player);
				player.teleport(tGold);
			} else if (i == 2) {
				i = 1;
				player.sendMessage(ChatColor.BLUE + "You are in team: Blue");

				player.getInventory().setHelmet(new ItemStack(Material.WOOL.getId(), 1, (short) 0, (byte) 3));
				
				teamBlue.add(player);
				player.teleport(tBlue);
			}
		}
	}



	public boolean die(Player player) {
		if (lives.containsKey(player)) {
			int l = lives.get(player);
			l--;
			if (l >= 1) {
				System.out.println(l);
				lives.put(player, l);
				return true;
			} else {
				player.sendMessage(ChatColor.RED + "You died");
				dead.add(player);
				//player is dead.
				return false;
			}
		}
		return false;
	}

	public int getLives(Player player) {
		return lives.get(player);
	}

	public void spawn(Player player) {
		if (teamGold.contains(player)) {
			player.teleport(tGold);
		} else if (teamBlue.contains(player)) {
			player.teleport(tBlue);
		} else {

		}
	}


	public void end() {
		if (playing) {
			playing = false;
			dead.clear();
			teamGold.clear();
			teamBlue.clear();
			lives.clear();
		}
	}
}
