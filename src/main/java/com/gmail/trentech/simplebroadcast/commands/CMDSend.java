package com.gmail.trentech.simplebroadcast.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

import com.gmail.trentech.simplebroadcast.Main;

public class CMDSend implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		String message = args.<String> getOne("message").get();

		Main.instance().broadcast(Main.instance().processText(message));

		return CommandResult.success();
	}
}
