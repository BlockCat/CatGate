package me.blockcat.catgame.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import me.blockcat.catgame.CatArena;
import me.blockcat.catgame.CatConfig;
import me.blockcat.catgame.CatGame;
import me.blockcat.catgame.CatPlayer;
import me.blockcat.catgame.ListenerUtils.CatEvent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PVPHandler extends Handler {

	private CatGame plugin;

	public HashMap<Player, Integer> lives = new HashMap<Player, Integer>();
	public List<Player> teamGold = new ArrayList<Player>();
	public List<Player> teamBlue = new ArrayList<Player>();
	public HashMap<String, CatPlayer> players = new HashMap<String, CatPlayer>();
	public Location tGold;
	public Location tBlue;
	public boolean playing = false;
	public World arenaWorld;

	public PVPHandler(CatGame plugin) {
		this.plugin = plugin;

	}

	public void start() {
		CatArena arena = new CatArena(plugin);
		arenaWorld = arena.createArena();

		if (arenaWorld == null) {
			Bukkit.broadcastMessage(ChatColor.DARK_RED + "No arena found.");
			return;
		}

		Bukkit.broadcastMessage(ChatColor.AQUA + "Arena chosen: " + ChatColor.GREEN + arena.getName());

		tGold = arena.getLocG();
		tBlue = arena.getLocB();

		playing = true;
		int i = 1;
		for (Entry<String, CatPlayer> m : players.entrySet()) {
			Player player = m.getValue().getPlayer();
			m.getValue().setOldLoc(player.getLocation());
			m.getValue().setInventory(player.getInventory());
			player.setGameMode(GameMode.ADVENTURE);
			player.setHealth(20);
			player.setFoodLevel(20);

			lives.put(player, CatConfig.getLives());
			player.getInventory().clear();
			if (i == 1) {
				i = 2;
				player.sendMessage(ChatColor.GOLD + "You are in team: Gold");
				player.getInventory().setHelmet(
						new ItemStack(Material.WOOL.getId(), 1, (short) 0,
								(byte) 1));

				teamGold.add(player);
			} else if (i == 2) {
				i = 1;
				player.sendMessage(ChatColor.BLUE + "You are in team: Blue");

				player.getInventory().setHelmet(
						new ItemStack(Material.WOOL.getId(), 1, (short) 0,
								(byte) 3));

				teamBlue.add(player);
			}
			this.spawn(player);
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
				players.get(player.getName()).setAlive(false);
				for (Player p : Bukkit.getOnlinePlayers()) {
					p.hidePlayer(player);
				}
				// player is dead.
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
			for (Entity ent :Bukkit.getWorld(CatConfig.getWorldName()).getEntities()) {
				if (ent instanceof Item) {
					ent.remove();
				}
			}
			for (Entry<String, CatPlayer> m : players.entrySet()) {
				m.getValue().endGame();
				for (Player p : Bukkit.getOnlinePlayers()) {
					p.showPlayer(m.getValue().getPlayer());
				}
			}

			teamGold.clear();
			teamBlue.clear();
			players.clear();
			lives.clear();
			World world = Bukkit.getServer().getWorld(CatConfig.getWorldName());
			Bukkit.getServer().unloadWorld(world, world.isAutoSave());
			File f = plugin.getDataFolder().getAbsoluteFile().getParentFile().getParentFile();
			File dir = new File(f + File.separator + CatConfig.getWorldName());
			if (dir.isDirectory()) {
				for (File file : dir.listFiles()) {
					file.delete();
				}
				dir.delete();
			}

		}
	}
	
	public boolean isAlive(Player player) {
		return players.get(player.getName()).isAlive();
	}

	public boolean canStart(Player sender) {
		boolean start = true;
		if (!players.containsKey(sender.getName())) {
			sender.sendMessage(ChatColor.RED
					+ "You must enter first with: /cg enter");
			return false;
		} else {
			players.get(sender.getName()).setSet(true);
			sender.sendMessage(ChatColor.GREEN + "You entered the game");
		}
		for (Entry<String, CatPlayer> m : players.entrySet()) {
			if (!m.getValue().isSet()) {
				start = false;
			}
		}
		//minimum
		if (players.size() < CatConfig.getMinimum()) {
			return false;
		}
		return start;
	}

	@CatEvent(event=CreatureSpawnEvent.class)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (event.getEntity().getWorld().getName()
				.equalsIgnoreCase(CatConfig.getWorldName())) {
			event.getEntity().remove();
			event.setCancelled(true);
		}
	}

	@CatEvent(event=PlayerCommandPreprocessEvent.class)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		Player p = event.getPlayer();
		if (p.getWorld().getName().equalsIgnoreCase(CatConfig.getWorldName())) {

			if (p.isOp()) return;

			if (!event.getMessage().split(" ")[0].equalsIgnoreCase("/cg")) {
				event.setCancelled(true);
				p.sendMessage(ChatColor.RED
						+ "You can't use that command right now!");
			}
		}
	}

	@CatEvent(event=PlayerInteractEvent.class)
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if (p.getWorld().getName().equalsIgnoreCase(CatConfig.getWorldName())) {

			p.setFoodLevel(20);
		}
	}

	@CatEvent(event=InventoryClickEvent.class)
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getWhoClicked().getWorld().getName()
				.equalsIgnoreCase(CatConfig.getWorldName()))
			if (event.getSlotType() == SlotType.ARMOR)
				event.setCancelled(true);
	}

	@CatEvent(event=EntityDamageEvent.class) 
	public void onEntityDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player)) return;
		
		Player victim = (Player) event.getEntity();
		PVPHandler catpvp = plugin.catpvp;
		if (event.getCause() == DamageCause.ENTITY_ATTACK) return;

		if(victim.getWorld().getName() == CatConfig.getWorldName()) {
			if (event.getDamage() >= victim.getHealth()) {
				event.setCancelled(true);
				if (catpvp.die(victim)) {
					victim.sendMessage(ChatColor.RED
							+ "You died and have: "
							+ catpvp.getLives(victim) + " lives left");

					catpvp.spawn(victim);
					victim.setHealth(20);

				} else {
					catpvp.players.get(victim.getName()).setAlive(false);
					if (catpvp.teamBlue.contains(victim)) {
						catpvp.teamBlue.remove(victim);
					}
					if (catpvp.teamGold.contains(victim)) {
						catpvp.teamGold.remove(victim);
					}
					if (catpvp.teamBlue.isEmpty()) {
						Bukkit.broadcastMessage(ChatColor.AQUA
								+ "Team: Gold is the winner!");
						catpvp.end();
					} else if (catpvp.teamGold.isEmpty()) {
						Bukkit.broadcastMessage(ChatColor.AQUA
								+ "Team: Blue is the winner!");
						catpvp.end();
					}
					victim.getInventory().setHelmet(new ItemStack(0));
				}
			}
		}

	}

	@CatEvent(event=EntityDamageByEntityEvent.class)
	public void onEntityDamagedByEntity(EntityDamageByEntityEvent event) {

		Entity ent = event.getEntity();
		Player attacker = null;
		Player victim = null;

		PVPHandler catpvp = plugin.catpvp;

		if (!catpvp.playing)
			return;
		if (!ent.getWorld().getName()
				.equalsIgnoreCase(CatConfig.getWorldName()))
			return;

		Entity damager = event.getDamager();

		//damager is arrow?
		if (damager instanceof Arrow) {
			Arrow arrow = (Arrow) damager;
			attacker = (Player) arrow.getShooter();

			//damager is player?	
		} else if (damager instanceof Player) {
			attacker = (Player) damager;
			//damager is not important, cancel
		} else {
			return;
		}

		if (catpvp.players.containsKey(attacker.getName())) {
			if (!catpvp.players.get(attacker.getName()).isAlive())
				event.setCancelled(true);
		}

		if (ent instanceof Player) {
			victim = (Player) ent;
		} else {
			return;
		}

		int damage = event.getDamage();

		if (catpvp.players.containsKey(attacker.getName()))
			if (catpvp.players.get(attacker.getName()).isAlive())
				if (victim.getHealth() <= damage) {

					event.setDamage(0);
					attacker.sendMessage(ChatColor.GREEN + "You killed: "
							+ victim.getName());

					if (catpvp.die(victim)) {
						victim.sendMessage(ChatColor.RED
								+ "You died and have: "
								+ catpvp.getLives(victim) + " lives left");

						catpvp.spawn(victim);
						catpvp.spawn(attacker);
						victim.setHealth(20);

					} else {
						catpvp.players.get(victim.getName()).setAlive(false);
						if (catpvp.teamBlue.contains(victim)) {
							catpvp.teamBlue.remove(victim);
						}
						if (catpvp.teamGold.contains(victim)) {
							catpvp.teamGold.remove(victim);
						}
						if (catpvp.teamBlue.isEmpty()) {
							Bukkit.broadcastMessage(ChatColor.AQUA
									+ "Team: Gold is the winner!");
							catpvp.end();
						} else if (catpvp.teamGold.isEmpty()) {
							Bukkit.broadcastMessage(ChatColor.AQUA
									+ "Team: Blue is the winner!");
							catpvp.end();
						}
						victim.getInventory().setHelmet(new ItemStack(0));
					}
				}
	}	


}
