package me.blockcat.catgate;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CatCommands implements CommandExecutor {

	private CatGate plugin;

	public CatCommands(CatGate plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if (cmd.getName().equalsIgnoreCase("start") && sender.isOp()) {
			if (!plugin.catpvp.playing)
			plugin.catpvp.start();
		}
		return false;
	}

}
