package com.gmail.trentech.simplebroadcast.commands;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationList.Builder;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.simplebroadcast.Broadcast;
import com.gmail.trentech.simplebroadcast.utils.Help;

public class CMDList implements CommandExecutor {

	public CMDList() {
		Help help = new Help("list", "list", " List all broadcast messages");
		help.setPermission("simplebroadcast.cmd.broadcast.list");
		help.setSyntax(" /broadcast list\n /b l");
		help.setExample(" /broadcast list");
		help.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		List<Text> list = new ArrayList<>();

		int i = 0;
		for (Text message : Broadcast.getBroadcasts()) {
			list.add(Text.of(TextColors.GREEN, "[", i, "] ", TextColors.RESET, message));
			i++;
		}

		if (src instanceof Player) {
			Builder pages = PaginationList.builder();

			pages.title(Text.builder().color(TextColors.DARK_GREEN).append(Text.of(TextColors.GREEN, "Messages")).build());

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
