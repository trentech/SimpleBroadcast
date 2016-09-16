package com.gmail.trentech.simplebroadcast.commands;

import java.util.Set;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.simplebroadcast.utils.ConfigManager;
import com.gmail.trentech.simplebroadcast.utils.Help;

import ninja.leaping.configurate.ConfigurationNode;

public class CMDStop implements CommandExecutor {

	public CMDStop() {
		Help help = new Help("stop", "stop", " Toggle off auto broadcasts");
		help.setPermission("simplebroadcast.cmd.broadcast.stop");
		help.setSyntax(" /broadcast stop\n /b off");
		help.setExample(" /broadcast stop\n");
		help.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		ConfigManager configManager = ConfigManager.get();
		ConfigurationNode config = configManager.getConfig();

		config.getNode("broadcast", "enable").setValue(false);
		configManager.save();

		Set<Task> tasks = Sponge.getScheduler().getScheduledTasks();

		for (Task task : tasks) {
			if (task.getName().equalsIgnoreCase("broadcast")) {
				task.cancel();
				break;
			}
		}

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Auto Broadcast Disabled"));

		return CommandResult.success();
	}

}
