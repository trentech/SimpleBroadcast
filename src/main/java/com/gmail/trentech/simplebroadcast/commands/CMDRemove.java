package com.gmail.trentech.simplebroadcast.commands;

import java.util.List;
import java.util.stream.Collectors;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.simplebroadcast.Broadcast;
import com.gmail.trentech.simplebroadcast.utils.ConfigManager;
import com.gmail.trentech.simplebroadcast.utils.Help;

import ninja.leaping.configurate.ConfigurationNode;

public class CMDRemove implements CommandExecutor {

	public CMDRemove() {
		Help help = new Help("remove", "remove", " Remove message from broadcast list");
		help.setSyntax(" /broadcast remove <index>\n /b r <message>");
		help.setExample(" /broadcast remove 4");
		help.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		int index = args.<Integer> getOne("index").get();

		ConfigManager configManager = ConfigManager.get();

		List<Text> broadcasts = Broadcast.getBroadcasts();

		broadcasts.remove(index);

		ConfigurationNode node = configManager.getConfig().getNode("broadcast", "messages");

		List<String> list = node.getChildrenList().stream().map(ConfigurationNode::getString).collect(Collectors.toList());
		list.remove(index);

		node.setValue(list);

		configManager.save();

		src.sendMessage(Text.of(TextColors.GREEN, "Message saved"));

		return CommandResult.success();
	}

}
