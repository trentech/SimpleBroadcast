package com.gmail.trentech.simplebroadcast.commands;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationList.Builder;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.helpme.help.Help;

public class CMDBroadcast implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (Sponge.getPluginManager().isLoaded("helpme")) {
			Help.executeList(src, Help.get("broadcast").get().getChildren());
			
			return CommandResult.success();
		}
		
		List<Text> list = new ArrayList<>();

		if (src.hasPermission("simplebroadcast.cmd.broadcast.add")) {
			list.add(Text.builder().color(TextColors.GREEN).onClick(TextActions.runCommand("/simplebroadcast:broadcast add")).append(Text.of(" /broadcast add")).build());
		}
		if (src.hasPermission("simplebroadcast.cmd.broadcast.remove")) {
			list.add(Text.builder().color(TextColors.GREEN).onClick(TextActions.runCommand("/simplebroadcast:broadcast remove")).append(Text.of(" /broadcast remove")).build());
		}
		if (src.hasPermission("simplebroadcast.cmd.broadcast.mute")) {
			list.add(Text.builder().color(TextColors.GREEN).onClick(TextActions.runCommand("/simplebroadcast:broadcast mute")).append(Text.of(" /broadcast mute")).build());
		}
		if (src.hasPermission("simplebroadcast.cmd.broadcast.unmute")) {
			list.add(Text.builder().color(TextColors.GREEN).onClick(TextActions.runCommand("/simplebroadcast:broadcast unmute")).append(Text.of(" /broadcast unmute")).build());
		}
		if (src.hasPermission("simplebroadcast.cmd.broadcast.list")) {
			list.add(Text.builder().color(TextColors.GREEN).onClick(TextActions.runCommand("/simplebroadcast:broadcast list")).append(Text.of(" /broadcast list")).build());
		}
		if (src.hasPermission("simplebroadcast.cmd.broadcast.start")) {
			list.add(Text.builder().color(TextColors.GREEN).onClick(TextActions.runCommand("/simplebroadcast:broadcast start")).append(Text.of(" /broadcast start")).build());
		}
		if (src.hasPermission("simplebroadcast.cmd.broadcast.stop")) {
			list.add(Text.builder().color(TextColors.GREEN).onClick(TextActions.runCommand("/simplebroadcast:broadcast stop")).append(Text.of(" /broadcast stop")).build());
		}
		if (src.hasPermission("simplebroadcast.cmd.broadcast.send")) {
			list.add(Text.builder().color(TextColors.GREEN).onClick(TextActions.runCommand("/simplebroadcast:broadcast send")).append(Text.of(" /broadcast send")).build());
		}

		if (src instanceof Player) {
			Builder pages = PaginationList.builder();

			pages.title(Text.builder().color(TextColors.DARK_GREEN).append(Text.of(TextColors.GREEN, "Command List")).build());

			pages.contents(list);

			pages.sendTo(src);
		} else {
			for (Text text : list) {
				src.sendMessage(text);
			}
		}

		return CommandResult.success();
	}

}
