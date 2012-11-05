package me.blockcat.catgame;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class CatPlayer {
	
	private Location oldLoc;
	private Player player;
	private boolean alive;
	private boolean isSet;
	private PlayerInventory inventory;
	
	public CatPlayer(Player player) {
		oldLoc = player.getLocation();
		this.player = player;
		alive = true;
		isSet = false;
		setInventory(player.getInventory());
	}
	
	public void endGame() {
		player.teleport(oldLoc);
		player.getInventory().clear();
		for(ItemStack is : inventory.getContents()) {
			player.getInventory().addItem(is);
		}
	}
	
	
	public Location getOldLoc() {
		return oldLoc;
	}
	public void setOldLoc(Location oldLoc) {
		this.oldLoc = oldLoc;
	}
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public boolean isAlive() {
		return alive;
	}
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	public boolean isSet() {
		return isSet;
	}
	public void setSet(boolean isSet) {
		this.isSet = isSet;
	}


	public PlayerInventory getInventory() {
		return inventory;
	}


	public void setInventory(PlayerInventory inventory) {
		this.inventory = inventory;
	}

}
