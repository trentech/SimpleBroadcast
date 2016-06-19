package com.gmail.trentech.simplebroadcast.commands;

import java.util.Set;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.simplebroadcast.Broadcast;
import com.gmail.trentech.simplebroadcast.Main;
import com.gmail.trentech.simplebroadcast.utils.ConfigManager;
import com.gmail.trentech.simplebroadcast.utils.Help;

import ninja.leaping.configurate.ConfigurationNode;

public class CMDStart implements CommandExecutor {

	public CMDStart(){
		Help help = new Help("start", "start", " Toggle on auto broadcasts and set time in minutes.");
		help.setSyntax(" /broadcast start [time]\n /b on [time]");
		help.setExample(" /broadcast start\n /broadcast start 5");
		help.save();
	}
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		ConfigManager configManager = new ConfigManager();
		ConfigurationNode config = configManager.getConfig();

		config.getNode("broadcast", "enable").setValue(true);
		
		if(args.hasAny("time")){
			String value = args.<String>getOne("time").get();
			
			try{
				config.getNode("broadcast", "minutes").setValue(Integer.parseInt(value));
			}catch(Exception e){
				src.sendMessage(Text.of(TextColors.DARK_RED, value, " is not a valid integer"));
				return CommandResult.empty();
			}
		}
		
		configManager.save();

		Set<Task> tasks = Main.getGame().getScheduler().getScheduledTasks();
		
		for(Task task : tasks){
			if(task.getName().equalsIgnoreCase("broadcast")){
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