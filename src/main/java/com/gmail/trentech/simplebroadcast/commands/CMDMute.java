package com.gmail.trentech.simplebroadcast.commands;

import java.util.List;
import java.util.stream.Collectors;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.simplebroadcast.utils.ConfigManager;
import com.gmail.trentech.simplebroadcast.utils.Help;

import ninja.leaping.configurate.ConfigurationNode;

public class CMDMute implements CommandExecutor {

	public CMDMute() {
		Help help = new Help("mute", "mute", " Allow player to mute broadcasts");
		help.setSyntax(" /broadcast mute\n /b m");
		help.setExample(" /broadcast mute");
		help.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (!(src instanceof Player)) {
			src.sendMessage(Text.of(TextColors.RED, "Must be a player"));
			return CommandResult.empty();
		}
		Player player = (Player) src;
		String uuid = player.getUniqueId().toString();
		
		ConfigManager configManager = new ConfigManager("mute");
		ConfigurationNode node = configManager.getConfig().getNode("players");
		
		List<String> list = node.getChildrenList().stream().map(ConfigurationNode::getString).collect(Collectors.toList());
		
		if(!list.contains(uuid)) {
			list.add(uuid);
			node.setValue(list);
			configManager.save();
		}

		src.sendMessage(Text.of(TextColors.GREEN, "Muted broadcasts"));

		return CommandResult.success();
	}

}
