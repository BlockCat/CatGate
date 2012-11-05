package me.blockcat.catgame;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CatCommands implements CommandExecutor {

	private CatGame plugin;

	public CatCommands(CatGame plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2,
			String[] args) {

		if (args.length == 0) {
			sender.sendMessage(ChatColor.AQUA + "PvP game commands:");
			sender.sendMessage(ChatColor.GREEN + "/cg " + ChatColor.AQUA
					+ "enter" + ChatColor.BLUE + ": to enter the game.");
			sender.sendMessage(ChatColor.GREEN + "/cg " + ChatColor.AQUA
					+ "start" + ChatColor.BLUE + ": to start the game.");
			sender.sendMessage(ChatColor.GREEN + "/cg " + ChatColor.AQUA
					+ "list" + ChatColor.BLUE + ": to see who is participating.");
			sender.sendMessage(ChatColor.GREEN + "/cg " + ChatColor.AQUA
					+ "leave" + ChatColor.BLUE + ": to leave the game.");
			sender.sendMessage(ChatColor.GREEN + "/cg " + ChatColor.AQUA
					+ "end" + ChatColor.BLUE + ": to end the game."
					+ ChatColor.RED + "(Only for OP's");
			return true;
		}

		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player) sender;

		boolean canPlay = CatGame.hasPerms(player, "catgame.play");

		if (!canPlay) {
			player.sendMessage(ChatColor.RED
					+ "You do not have permissions to participate in a game.");
			return true;
		}

		if (args[0].equalsIgnoreCase("start")) {
			if (!plugin.catpvp.playing) {
				if (plugin.catpvp.canStart((Player) sender)) {
					plugin.catpvp.start();
					Bukkit.broadcastMessage(ChatColor.GREEN
							+ "A new game has started!");
					return true;
				}
			} else {
				sender.sendMessage(ChatColor.RED
						+ "People already are playing.");
				return true;
			}
		} else if(args[0].equalsIgnoreCase("list")) {
			player.sendMessage(ChatColor.GOLD + "People playing:");
			if (plugin.catpvp.playing) {
				for (Player p : plugin.catpvp.teamBlue) {
					String m = plugin.catpvp.isAlive(p) ? (ChatColor.GREEN +"Playing") : (ChatColor.RED + "Dead");
					player.sendMessage(ChatColor.AQUA + p.getDisplayName() + ": " + m);
				}
				for (Player p : plugin.catpvp.teamGold) {
					String m = plugin.catpvp.isAlive(p) ? (ChatColor.GREEN +"Playing") : (ChatColor.RED + "Dead");
					player.sendMessage(ChatColor.GOLD + p.getDisplayName() + ": " + m);
				}
			} else {
				for (Entry<String, CatPlayer> m : plugin.catpvp.players.entrySet()) {
					String done = m.getValue().isSet()? (ChatColor.GOLD + "Ready.") : (ChatColor.AQUA + "Waiting...");
					player.sendMessage(ChatColor.GREEN + m.getKey() +": " + done);
				}
			}				
		} else if (args[0].equalsIgnoreCase("end")
				&& CatGame.hasPerms(player, "catgame.end")) {
			if (plugin.catpvp.playing) {
				Bukkit.broadcastMessage(ChatColor.RED + sender.getName()
						+ " has ended the game!");
				plugin.catpvp.end();
			} else {
				sender.sendMessage(ChatColor.RED + "there is no game going on.");
			}
		} else if (args[0].equalsIgnoreCase("leave")) {
			if (plugin.catpvp.playing)
			if (plugin.catpvp.players.containsKey(sender.getName())) {
				CatPlayer cPlayer = plugin.catpvp.players.get(sender.getName());
				cPlayer.endGame();
				plugin.catpvp.players.remove(sender.getName());
				if (plugin.catpvp.players.size() == 1) {
					if (plugin.catpvp.teamBlue.isEmpty()) {
						Bukkit.broadcastMessage(ChatColor.AQUA
								+ "Team: Gold is the winner!");
						plugin.catpvp.playing = false;
					} else if (plugin.catpvp.teamGold.isEmpty()) {
						Bukkit.broadcastMessage(ChatColor.AQUA
								+ "Team: Blue is the winner!");
						plugin.catpvp.playing = false;
					}
				} else if (plugin.catpvp.players.isEmpty()) {
					Bukkit.broadcastMessage(ChatColor.AQUA + "It became a draw!");
					plugin.catpvp.playing = false;
				}
			}

		} else if (args[0].equalsIgnoreCase("enter")) {
			if (sender instanceof Player) {
				if (!plugin.catpvp.playing) {
					if (plugin.catpvp.players.containsKey(sender.getName())) {
						sender.sendMessage(ChatColor.RED + "You already are signed up.");
					}
					plugin.catpvp.players.put(sender.getName(), new CatPlayer((Player) sender));
					sender.sendMessage(ChatColor.RED
							+ "You entered. /cg start to start");
				} else {
					sender.sendMessage(ChatColor.RED
							+ "People already are playing.");
					return true;
				}
			}

		}
		return true;

	}

}
