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

import com.gmail.trentech.simplebroadcast.Broadcast;
import com.gmail.trentech.simplebroadcast.utils.ConfigManager;

import ninja.leaping.configurate.ConfigurationNode;

public class CMDStart implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		ConfigManager configManager = ConfigManager.get();
		ConfigurationNode config = configManager.getConfig();

		config.getNode("broadcast", "enable").setValue(true);

		if (args.hasAny("time")) {
			int time = args.<Integer> getOne("time").get();

			config.getNode("broadcast", "minutes").setValue(time);
		}

		configManager.save();

		Set<Task> tasks = Sponge.getScheduler().getScheduledTasks();

		for (Task task : tasks) {
			if (task.getName().equalsIgnoreCase("broadcast")) {
				configManager.save();
				src.sendMessage(Text.of(TextColors.DARK_GREEN, "Auto Broadcast Enabled"));
				return CommandResult.success();
			}
		}

		new Broadcast().start(config);

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Auto Broadcast Enabled"));

		return CommandResult.success();
	}

}
