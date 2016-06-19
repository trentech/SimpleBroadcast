package com.gmail.trentech.simplebroadcast.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.simplebroadcast.Main;
import com.gmail.trentech.simplebroadcast.utils.Help;

public class CMDSend implements CommandExecutor {

	public CMDSend(){
		Help help = new Help("send", "send", " Broadcast a message");
		help.setSyntax(" /broadcast send <message>\n /b s <message>");
		help.setExample(" /broadcast send Hello world!");
		help.save();
	}
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(!args.hasAny("message")) {
			src.sendMessage(Text.of(TextColors.YELLOW, "/broadcast send <message>"));
			return CommandResult.empty();
		}
		String message = args.<String>getOne("message").get();

		Main.broadcast(Main.processText(message));

		return CommandResult.success();
	}
}
