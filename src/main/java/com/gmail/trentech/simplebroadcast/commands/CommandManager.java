package com.gmail.trentech.simplebroadcast.commands;

import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class CommandManager {

	private CommandSpec cmdAdd = CommandSpec.builder().permission("simplebroadcast.cmd.broadcast.add").arguments(GenericArguments.optional(GenericArguments.remainingJoinedStrings(Text.of("message")))).executor(new CMDAdd()).build();

	private CommandSpec cmdRemove = CommandSpec.builder().permission("simplebroadcast.cmd.broadcast.remove").arguments(GenericArguments.optional(GenericArguments.string(Text.of("index")))).executor(new CMDRemove()).build();

	private CommandSpec cmdList = CommandSpec.builder().permission("simplebroadcast.cmd.broadcast.list").executor(new CMDList()).build();

	private CommandSpec cmdStart = CommandSpec.builder().permission("simplebroadcast.cmd.broadcast.start").arguments(GenericArguments.optional(GenericArguments.string(Text.of("time")))).executor(new CMDStart()).build();

	private CommandSpec cmdStop = CommandSpec.builder().permission("simplebroadcast.cmd.broadcast.stop").executor(new CMDStop()).build();

	public CommandSpec cmdSend = CommandSpec.builder().permission("simplebroadcast.cmd.broadcast.send").arguments(GenericArguments.optional(GenericArguments.remainingJoinedStrings(Text.of("message")))).executor(new CMDSend()).build();

	public CommandSpec cmdBroadcast = CommandSpec.builder().permission("simplebroadcast.cmd.broadcast").child(cmdAdd, "add", "a").child(cmdRemove, "remove", "r").child(cmdList, "list", "l").child(cmdStart, "start", "on").child(cmdStop, "stop", "off").child(cmdSend, "send", "s").executor(new CMDBroadcast()).build();
}
