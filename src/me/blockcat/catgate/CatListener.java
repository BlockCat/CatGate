package me.blockcat.catgate;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent.Result;
import org.bukkit.inventory.ItemStack;

public class CatListener implements Listener {


	private CatGate plugin;

	public CatListener(CatGate plugin) {
		this.plugin = plugin;
	}


	//Arrow behaving same as sword
	//food bar 
	
	
	@EventHandler
	public void onPlayerPreLogin(PlayerPreLoginEvent event ) {
		if (plugin.catpvp.playing) {
			event.disallow(Result.KICK_OTHER, ChatColor.DARK_RED + "At the moment people are playing.");
		}
	}
	
	//handles cGates.
	@EventHandler
	public void onRedstoneChange(BlockRedstoneEvent e) {
		Block block = e.getBlock();

		int startX = block.getX()-1;
		int startY = block.getY()-1;
		int startZ = block.getZ()-1;
		int endX = block.getX()+1;
		int endY = block.getY()+1;
		int endZ = block.getZ()+1;
		for(int x = startX; x <= endX; x++)
			for(int y = startY; y <= endY; y++)
				for(int z = startZ; z <= endZ; z++) {
					Block b = block.getWorld().getBlockAt(x,y,z);
					if (b.getTypeId() == 63 || b.getTypeId() == 68)
					{
						Sign sign = (Sign) b.getState();
						if (sign.getLine(0).equalsIgnoreCase("[cGate]")) {
							if (b.isBlockPowered()){
								new CGate(b).up();
							} else {
								new CGate(b).down();
							}
						}
					}		
				}
	}

	//sets player in Adventure mode.
	@EventHandler
	public void onPlayerJoin (PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (!player.isOp())
			player.setGameMode(GameMode.ADVENTURE);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (plugin.catpvp.dead.contains(event.getPlayer()))
			event.setCancelled(true);
		
		event.getPlayer().setFoodLevel(20);
	
	}

	//sets animalls invincible
	@EventHandler
	public void onEntityDamagedByEntity (EntityDamageByEntityEvent event) {
		Entity ent = event.getEntity();
		if (ent instanceof Cow) {
			event.setDamage(0);
			return;
		}
		System.out.println("damage");
		CatPvP catpvp = plugin.catpvp;

		Entity attacker = event.getDamager();
		//if player is dead, no hitting.
		
		if (attacker instanceof Arrow && catpvp.playing) {
			
			Arrow arrow = (Arrow) attacker;
			Player att = (Player) arrow.getShooter();
			if (catpvp.dead.contains(att)) {
				event.setCancelled(true);
			}
			if (ent instanceof Player) {
				Player victim = (Player) ent;
				int damage = event.getDamage();
				if (victim.getHealth() <= damage) {
					event.setDamage(0);
					att.sendMessage(ChatColor.GREEN + "You killed: " + victim.getName());
					if (catpvp.die(victim)) {
						victim.sendMessage(ChatColor.RED + "You died and have: " + catpvp.getLives(victim) + " lives left");
						catpvp.spawn(victim);
						victim.setHealth(20);
					} else {
						if (catpvp.teamBlue.contains(victim)) {
							catpvp.teamBlue.remove(victim);
							System.out.println("removed from tb");
						}
						if (catpvp.teamGold.contains(victim)) {
							catpvp.teamGold.remove(victim);
							System.out.println("removed from tg");
						}
						if (catpvp.teamBlue.isEmpty()) {
							Bukkit.broadcastMessage(ChatColor.AQUA + "Team: Gold is the winner!");
							catpvp.end();							
						}
						if (catpvp.teamGold.isEmpty()) {
							Bukkit.broadcastMessage(ChatColor.AQUA + "Team: Blue is the winner!");
							catpvp.end();
						}
						victim.getInventory().setHelmet(new ItemStack(0));
					}
				}
			}
		}
		else if (attacker instanceof Player && catpvp.playing) {
			Player att = (Player) attacker;
			if (catpvp.dead.contains(att)) {
				event.setCancelled(true);
			}
			if (ent instanceof Player) {
				Player victim = (Player) ent;
				int damage = event.getDamage();
				if (victim.getHealth() <= damage) {
					event.setDamage(0);
					att.sendMessage(ChatColor.GREEN + "You killed: " + victim.getName());
					if (catpvp.die(victim)) {
						victim.sendMessage(ChatColor.RED + "You died and have: " + catpvp.getLives(victim) + " lives left");
						catpvp.spawn(victim);
						victim.setHealth(20);
					} else {
						if (catpvp.teamBlue.contains(victim)) {
							catpvp.teamBlue.remove(victim);
							System.out.println("removed from tb");
						}
						if (catpvp.teamGold.contains(victim)) {
							catpvp.teamGold.remove(victim);
							System.out.println("removed from tg");
						}
						if (catpvp.teamBlue.isEmpty()) {
							Bukkit.broadcastMessage(ChatColor.AQUA + "Team: Gold is the winner!");
							catpvp.end();							
						}
						if (catpvp.teamGold.isEmpty()) {
							Bukkit.broadcastMessage(ChatColor.AQUA + "Team: Blue is the winner!");
							catpvp.end();
						}
						victim.getInventory().setHelmet(new ItemStack(0));
					}
				}
			}
		}
	}

	
	@EventHandler
	public void onInventoryClick (InventoryClickEvent event) {
		if (event.getSlotType() == SlotType.ARMOR)
		event.setCancelled(true);
		
	}
}
