package com.gmail.trentech.simplebroadcast.commands;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList.Builder;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.simplebroadcast.Main;
import com.gmail.trentech.simplebroadcast.utils.Help;

public class CMDBroadcast implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		List<Text> list = new ArrayList<>();

		if(src.hasPermission("simplebroadcast.cmd.broadcast.add")) {
			list.add(Text.builder().color(TextColors.GREEN).onHover(TextActions.showText(Text.of("Click command for more information ")))
					.onClick(TextActions.executeCallback(Help.getHelp("add"))).append(Text.of(" /broadcast add")).build());
		}
		if(src.hasPermission("simplebroadcast.cmd.broadcast.remove")) {
			list.add(Text.builder().color(TextColors.GREEN).onHover(TextActions.showText(Text.of("Click command for more information ")))
					.onClick(TextActions.executeCallback(Help.getHelp("remove"))).append(Text.of(" /broadcast remove")).build());
		}
		if(src.hasPermission("simplebroadcast.cmd.broadcast.list")) {
			list.add(Text.builder().color(TextColors.GREEN).onHover(TextActions.showText(Text.of("Click command for more information ")))
					.onClick(TextActions.executeCallback(Help.getHelp("list"))).append(Text.of(" /broadcast list")).build());
		}
		if(src.hasPermission("simplebroadcast.cmd.broadcast.start")) {
			list.add(Text.builder().color(TextColors.GREEN).onHover(TextActions.showText(Text.of("Click command for more information ")))
					.onClick(TextActions.executeCallback(Help.getHelp("start"))).append(Text.of(" /broadcast start")).build());
		}
		if(src.hasPermission("simplebroadcast.cmd.broadcast.stop")) {
			list.add(Text.builder().color(TextColors.GREEN).onHover(TextActions.showText(Text.of("Click command for more information ")))
					.onClick(TextActions.executeCallback(Help.getHelp("stop"))).append(Text.of(" /broadcast stop")).build());
		}
		if(src.hasPermission("simplebroadcast.cmd.broadcast.send")) {
			list.add(Text.builder().color(TextColors.GREEN).onHover(TextActions.showText(Text.of("Click command for more information ")))
					.onClick(TextActions.executeCallback(Help.getHelp("send"))).append(Text.of(" /broadcast send")).build());
		}
		
		if(src instanceof Player) {
			Builder pages = Main.getGame().getServiceManager().provide(PaginationService.class).get().builder();

			pages.title(Text.builder().color(TextColors.DARK_GREEN).append(Text.of(TextColors.GREEN, "Command List")).build());
			
			pages.contents(list);
			
			pages.sendTo(src);
		}else {
			for(Text text : list) {
				src.sendMessage(text);
			}
		}

		return CommandResult.success();
	}

}
