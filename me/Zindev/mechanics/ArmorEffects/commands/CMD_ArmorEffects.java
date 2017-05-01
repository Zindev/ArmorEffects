package me.Zindev.mechanics.ArmorEffects.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.Zindev.mechanics.ArmorEffects.MainArmor;

public class CMD_ArmorEffects implements CommandExecutor{

	private static List<String> cmds = Arrays.asList("reload");
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length >= 1 && args[0].equalsIgnoreCase("reload")){
			MainArmor.reload();
			sender.sendMessage(ChatColor.LIGHT_PURPLE+"Config.yml has been reloaded.");
		}
		if(args.length == 0){
			sender.sendMessage(ChatColor.DARK_RED+"---ArmorEffect Commands---");
			cmds.forEach(s->sender.sendMessage(ChatColor.RED+"/"+label+" "+s));
		}
		return true;
	}

}
