package com.gmail.trentech.simplebroadcast.utils;

import org.spongepowered.api.Sponge;

import com.gmail.trentech.helpme.help.Argument;
import com.gmail.trentech.helpme.help.Help;
import com.gmail.trentech.helpme.help.Usage;

public class CommandHelp {

	public static void init() {
		if (Sponge.getPluginManager().isLoaded("helpme")) {
			Usage usageAdd = new Usage(Argument.of("<message>", "Specifies message to be added"));
			
			Help broadcastAdd = new Help("broadcast add", "add", "Add message to broadcast list")
					.setPermission("simplebroadcast.cmd.broadcast.add")
					.setUsage(usageAdd)
					.addExample("/broadcast add Welcome to the server");
			
			Help broadcastList = new Help("broadcast list", "list", "List all broadcast messages")
					.setPermission("simplebroadcast.cmd.broadcast.list")
					.addExample("/broadcast list");
			
			Help broadcastMute = new Help("broadcast mute", "mute", "Allow player to mute broadcasts")
					.setPermission("simplebroadcast.cmd.broadcast.mute")
					.addExample("/broadcast mute");
			
			Usage usageRemove = new Usage(Argument.of("<index>", "Index of the message. You can find this with /sban list"));
			
			Help broadcastRemove = new Help("broadcast remove", "remove", "Remove message from broadcast list")
					.setPermission("simplebroadcast.cmd.broadcast.remove")
					.setUsage(usageRemove)
					.addExample(" /broadcast remove 4");
			
			Usage usageSend = new Usage(Argument.of("<message>", "Specifies message to be broadcasted"));
			
			Help broadcastSend = new Help("broadcast send", "send", "Broadcast a message")
					.setPermission("simplebroadcast.cmd.broadcast.send")
					.setUsage(usageSend)
					.addExample("/broadcast send Hello world!");
			
			Usage usageStart = new Usage(Argument.of("[time]", "Specifies the time in minutes"));
			
			Help broadcastStart = new Help("broadcast start", "start", "Toggle on auto broadcasts and set time in minutes.")
					.setPermission("simplebroadcast.cmd.broadcast.start")
					.setUsage(usageStart)
					.addExample("/broadcast start")
					.addExample("/broadcast start 5");
			
			Help broadcastStop = new Help("broadcast stop", "stop", "Toggle off auto broadcasts")
					.setPermission("simplebroadcast.cmd.broadcast.stop")
					.addExample("/broadcast stop");
			
			Help broadcastUnmute = new Help("broadcast unmute", "unmute", "Allow player to unmute broadcasts")
					.setPermission("simplebroadcast.cmd.broadcast.unmute")
					.addExample("/broadcast unmute");
			
			Help broadcast = new Help("broadcast", "broadcast", "Base command for SimpleBroadcast")
					.setPermission("simplebroadcast.cmd.broadcast")
					.addChild(broadcastUnmute)
					.addChild(broadcastStop)
					.addChild(broadcastStart)
					.addChild(broadcastSend)
					.addChild(broadcastRemove)
					.addChild(broadcastMute)
					.addChild(broadcastList)
					.addChild(broadcastAdd);
			
			Help.register(broadcast);
			
			Usage usageTag = new Usage(Argument.of("<tag>", "Set custom tag. Accepts color codes"));
			
			Help tagBroadcast = new Help("tag broadcast", "broadcast", "View and edit broadcast tags")
					.setPermission("simpletags.cmd.tag.broadcast")
					.setUsage(usageTag)
					.addExample("/tag broadcast")
					.addExample("/tag broadcast &e[broadcast]")
					.addExample("/tag broadcast reset");
			
			Help.register(Help.get("tag").get().addChild(tagBroadcast));
		}
	}
}
