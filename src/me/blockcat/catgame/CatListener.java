package me.blockcat.catgame;

import me.blockcat.catgame.ListenerUtils.LHandler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CatListener implements Listener {

	private CatGame plugin;
	private LHandler lhandler;

	public CatListener(CatGame plugin, LHandler lhandler) {
		this.plugin = plugin;
		this.lhandler = lhandler;
	}

	// Arrow behaving same as sword
	// food bar

	// handles cGates.

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		if (plugin.catpvp.players.containsKey(event.getPlayer().getName())) {
			plugin.catpvp.players.get(event.getPlayer().getName()).endGame();
		}
	}

	@EventHandler
	public void onRedstoneChange(BlockRedstoneEvent event) {
		lhandler.triggerEvent(event);

	}

	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		lhandler.triggerEvent(event);
	}

	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		lhandler.triggerEvent(event);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		lhandler.triggerEvent(event);
	}

	@EventHandler
	public void onEntityDamagedByEntity(EntityDamageByEntityEvent event) {
		lhandler.triggerEvent(event);
	}

	@EventHandler
	public void onEntityDamaged(EntityDamageEvent event) {
		lhandler.triggerEvent(event);
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		lhandler.triggerEvent(event);
	}
}
