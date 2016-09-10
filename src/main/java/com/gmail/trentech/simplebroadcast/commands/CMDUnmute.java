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

public class CMDUnmute implements CommandExecutor {

	public CMDUnmute() {
		Help help = new Help("unmute", "unmute", " Allow player to unmute broadcasts");
		help.setSyntax(" /broadcast unmute\n /b u");
		help.setExample(" /broadcast unmute");
		help.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (!(src instanceof Player)) {
			throw new CommandException(Text.of(TextColors.RED, "Must be a player"));
		}
		Player player = (Player) src;
		String uuid = player.getUniqueId().toString();
		
		ConfigManager configManager = ConfigManager.get("mute");
		ConfigurationNode node = configManager.getConfig().getNode("players");
				
		List<String> list = node.getChildrenList().stream().map(ConfigurationNode::getString).collect(Collectors.toList());
		
		if(list.contains(uuid)) {
			list.remove(uuid);
			node.setValue(list);
			configManager.save();
		}

		src.sendMessage(Text.of(TextColors.GREEN, "Unmuted broadcasts"));

		return CommandResult.success();
	}

}
