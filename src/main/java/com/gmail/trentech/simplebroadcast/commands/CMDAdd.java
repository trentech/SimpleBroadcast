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
import com.gmail.trentech.simplebroadcast.Main;
import com.gmail.trentech.simplebroadcast.utils.ConfigManager;
import com.gmail.trentech.simplebroadcast.utils.Help;

import ninja.leaping.configurate.ConfigurationNode;

public class CMDAdd implements CommandExecutor {

	public CMDAdd() {
		Help help = new Help("add", "add", " Add message to broadcast list");
		help.setPermission("simplebroadcast.cmd.broadcast.add");
		help.setSyntax(" /broadcast add <message>\n /b a <message>");
		help.setExample(" /broadcast add Welcome to the server");
		help.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		String message = args.<String> getOne("message").get();

		ConfigManager configManager = ConfigManager.get();

		List<Text> broadcasts = Broadcast.getBroadcasts();

		broadcasts.add(Main.instance().processText(message));

		ConfigurationNode node = configManager.getConfig().getNode("broadcast", "messages");

		List<String> list = node.getChildrenList().stream().map(ConfigurationNode::getString).collect(Collectors.toList());
		list.add(message);

		node.setValue(list);

		configManager.save();

		src.sendMessage(Text.of(TextColors.GREEN, "Message saved"));

		return CommandResult.success();
	}

}
