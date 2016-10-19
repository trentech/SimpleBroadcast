package com.gmail.trentech.simplebroadcast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.channel.MutableMessageChannel;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.gmail.trentech.helpme.Help;
import com.gmail.trentech.simplebroadcast.commands.CMDTagBroadcast;
import com.gmail.trentech.simplebroadcast.commands.CommandManager;
import com.gmail.trentech.simplebroadcast.utils.ConfigManager;
import com.gmail.trentech.simplebroadcast.utils.Resource;
import com.gmail.trentech.simpletags.tags.SingleTag;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import me.flibio.updatifier.Updatifier;
import ninja.leaping.configurate.ConfigurationNode;

@Updatifier(repoName = Resource.NAME, repoOwner = Resource.AUTHOR, version = Resource.VERSION)
@Plugin(id = Resource.ID, name = Resource.NAME, version = Resource.VERSION, description = Resource.DESCRIPTION, authors = Resource.AUTHOR, url = Resource.URL, dependencies = { @Dependency(id = "Updatifier", optional = true), @Dependency(id = "simpletags", optional = true), @Dependency(id = "helpme", optional = true) })
public class Main {

	@Inject @ConfigDir(sharedRoot = false)
    private Path path;

	@Inject
	private Logger log;

	private static PluginContainer plugin;
	private static Main instance;
	
	@Listener
	public void onPreInitializationEvent(GamePreInitializationEvent event) {
		plugin = Sponge.getPluginManager().getPlugin(Resource.ID).get();
		instance = this;
		
		try {			
			Files.createDirectories(path);		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Listener
	public void onInitializationEvent(GameInitializationEvent event) {
		Sponge.getCommandManager().register(this, new CommandManager().cmdBroadcast, "broadcast", "b");

		ConfigManager.init();
		ConfigManager.init("mute");

		if (Sponge.getPluginManager().isLoaded("simpletags")) {
			com.gmail.trentech.simpletags.Main.registerCommand(CMDTagBroadcast.cmd, "broadcast", "b");
		}

		Broadcast.init();
		
		if (Sponge.getPluginManager().isLoaded("helpme")) {
			Help broadcastAdd = new Help("broadcast add", "add", "Add message to broadcast list")
					.setPermission("simplebroadcast.cmd.broadcast.add")
					.addUsage("/broadcast add <message>")
					.addUsage("/b a <message>")
					.addExample("/broadcast add Welcome to the server");
			
			Help broadcastList = new Help("broadcast list", "list", "List all broadcast messages")
					.setPermission("simplebroadcast.cmd.broadcast.list")
					.addUsage("/broadcast list")
					.addUsage("/b ls")
					.addExample("/broadcast list");
			
			Help broadcastMute = new Help("broadcast mute", "mute", "Allow player to mute broadcasts")
					.setPermission("simplebroadcast.cmd.broadcast.mute")
					.addUsage("/broadcast mute")
					.addUsage("/b m")
					.addExample("/broadcast mute");
			
			Help broadcastRemove = new Help("broadcast remove", "remove", "Remove message from broadcast list")
					.setPermission("simplebroadcast.cmd.broadcast.remove")
					.addUsage("/broadcast remove <index>")
					.addUsage("/b r <index>")
					.addExample(" /broadcast remove 4");
			
			Help broadcastSend = new Help("broadcast send", "send", "Broadcast a message")
					.setPermission("simplebroadcast.cmd.broadcast.send")
					.addUsage("/broadcast send <message>")
					.addUsage("/b s <message>")
					.addExample("/broadcast send Hello world!");
			
			Help broadcastStart = new Help("broadcast start", "start", "Toggle on auto broadcasts and set time in minutes.")
					.setPermission("simplebroadcast.cmd.broadcast.start")
					.addUsage("/broadcast start [time]\n ")
					.addUsage("/b on [time]")
					.addExample("/broadcast start")
					.addExample("/broadcast start 5");
			
			Help broadcastStop = new Help("broadcast stop", "stop", "Toggle off auto broadcasts")
					.setPermission("simplebroadcast.cmd.broadcast.stop")
					.addUsage("/broadcast stop")
					.addUsage("/b off")
					.addExample("/broadcast stop");
			
			Help broadcastUnmute = new Help("broadcast unmute", "unmute", "Allow player to unmute broadcasts")
					.setPermission("simplebroadcast.cmd.broadcast.unmute")
					.addUsage("/broadcast unmute")
					.addUsage("/b u")
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
			
			Help tagBroadcast = new Help("tag broadcast", "broadcast", "View and edit broadcast tags")
					.setPermission("simpletags.cmd.tag.broadcast")
					.addUsage("/tag broadcast <tag>")
					.addUsage("/t g <tag>")
					.addExample("/tag broadcast\n \n ")
					.addExample("/tag broadcast &e[broadcast]")
					.addExample("/tag broadcast reset");
			
			Help.register(Help.get("tag").get().addChild(tagBroadcast));
		}
	}

	public Logger getLog() {
		return log;
	}

	public Path getPath() {
		return path;
	}
	
	public void broadcast(Text message) {
		MutableMessageChannel channel = Sponge.getServer().getBroadcastChannel().asMutable();

		List<String> list = ConfigManager.get("mute").getConfig().getNode("players").getChildrenList().stream().map(ConfigurationNode::getString).collect(Collectors.toList());
		
		for (MessageReceiver receiver : Lists.newArrayList(channel.getMembers())) {
			if (!(receiver instanceof Player)) {
				continue;
			}
			Player player = (Player) receiver;

			if (list.contains(player.getUniqueId().toString())) {
				channel.removeMember(receiver);
			}
		}

		if (Sponge.getPluginManager().isLoaded("simpletags")) {
			Optional<SingleTag> optionalTag = SingleTag.get(getPlugin().getId(), "broadcast");

			if (optionalTag.isPresent()) {
				message = Text.of(optionalTag.get().getTag(), TextColors.WHITE, " ", message);
			}
		} else {
			message = Text.of(TextColors.GOLD, "[BROADCAST]", TextColors.WHITE, " ", message);
		}

		channel.send(Text.of(message));
	}

	public Text processText(String msg) {
		Text message = Text.EMPTY;

		while (msg.contains("&u")) {
			message = Text.join(message, TextSerializers.FORMATTING_CODE.deserialize(msg.substring(0, msg.indexOf("&u{")).replace("&u{", "")));

			String work = msg.substring(msg.indexOf("&u{"), msg.indexOf("}")).replaceFirst("&u\\{", "").replaceFirst("}", "");

			message = Text.join(message, getLink(work));

			msg = msg.substring(msg.indexOf("}"), msg.length()).replaceFirst("}", "");
		}

		return Text.of(message, TextSerializers.FORMATTING_CODE.deserialize(msg));
	}

	private Text getLink(String link) {
		Text.Builder builder = Text.builder();
		String[] work = link.split(";");

		if (work.length != 3) {
			return Text.of(TextColors.RED, "Invalid TextAction detected");
		}

		if (work[0].equalsIgnoreCase("url")) {
			if (!work[1].toLowerCase().contains("http://") && !work[1].toLowerCase().contains("https://")) {
				work[1] = "http://" + work[1];
			}

			URL url = null;
			try {
				url = new URL(work[1]);
				builder.onClick(TextActions.openUrl(url)).append(TextSerializers.FORMATTING_CODE.deserialize(work[2]));
			} catch (MalformedURLException e) {
				return Text.of(TextColors.RED, "Invalid URL detected");
			}
		} else if (work[0].equalsIgnoreCase("cmd")) {
			builder.onClick(TextActions.runCommand(work[1])).append(TextSerializers.FORMATTING_CODE.deserialize(work[2]));
		} else if (work[0].equalsIgnoreCase("suggest")) {
			builder.onClick(TextActions.suggestCommand(work[1])).append(TextSerializers.FORMATTING_CODE.deserialize(work[2]));
		} else if (work[0].equalsIgnoreCase("hover")) {
			builder.onHover(TextActions.showText(TextSerializers.FORMATTING_CODE.deserialize(work[1]))).append(TextSerializers.FORMATTING_CODE.deserialize(work[2]));
		} else {
			return Text.of(TextColors.RED, "Invalid TextAction detected");
		}

		return builder.build();
	}
	
	public static PluginContainer getPlugin() {
		return plugin;
	}
	
	public static Main instance() {
		return instance;
	}
}
