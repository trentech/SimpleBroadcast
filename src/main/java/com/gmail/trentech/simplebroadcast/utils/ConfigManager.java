package com.gmail.trentech.simplebroadcast.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.gmail.trentech.simplebroadcast.Main;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class ConfigManager {

	private Path path;
	private CommentedConfigurationNode config;
	private ConfigurationLoader<CommentedConfigurationNode> loader;
	
	private static ConcurrentHashMap<String, ConfigManager> configManagers = new ConcurrentHashMap<>();

	private ConfigManager(String configName) {
		try {
			path = Main.instance().getPath().resolve(configName + ".conf");
			
			if (!Files.exists(path)) {		
				Files.createFile(path);
				Main.instance().getLog().info("Creating new " + path.getFileName() + " file...");
			}		
		} catch (IOException e) {
			e.printStackTrace();
		}

		load();
	}
	
	public static ConfigManager get(String configName) {
		return configManagers.get(configName);
	}
	
	public static ConfigManager get() {
		return configManagers.get("config");
	}
	
	public static ConfigManager init() {
		return init("config");
	}
	
	public static ConfigManager init(String configName) {
		ConfigManager configManager = new ConfigManager(configName);
		CommentedConfigurationNode config = configManager.getConfig();
		
		if(configName.equalsIgnoreCase("config")) {
			if (config.getNode("broadcast", "enable").isVirtual()) {
				config.getNode("broadcast", "enable").setValue(false);
			}
			if (config.getNode("broadcast", "minutes").isVirtual()) {
				config.getNode("broadcast", "minutes").setValue(1);
			}
			if (config.getNode("broadcast", "random").isVirtual()) {
				config.getNode("broadcast", "random").setValue(true);
			}
			if (config.getNode("broadcast", "messages").isVirtual()) {
				List<String> list = new ArrayList<String>();

				list.add("&eWelcome to &5SERVER NAME&e! This is an auto broacast!");
				list.add("&eBreak stone blocks with a pickaxe!");
				list.add("&eYou can delete these message and create your own!");
				list.add("&eCreate clickable link &u{url;www.google.com;&1Click Here}&e like so");
				list.add("&eCreate clickable command &u{cmd;/say hello world;&1Click Here}&e like so");
				list.add("&eCreate clickable suggested command &u{suggest;/say hello world;&1Click Here}&e like so");
				list.add("&eCreate hovered text &u{hover;&3secret text;&1Hover Here}&e like so");

				config.getNode("broadcast", "messages").setValue(list);
			}
		}
		
		configManager.save();
		
		return configManager;		
	}
	
	public ConfigurationLoader<CommentedConfigurationNode> getLoader() {
		return loader;
	}

	public CommentedConfigurationNode getConfig() {
		return config;
	}

	public void save() {
		try {
			loader.save(config);
		} catch (IOException e) {
			Main.instance().getLog().error("Failed to save config");
			e.printStackTrace();
		}
	}

	private void load() {
		loader = HoconConfigurationLoader.builder().setPath(path).build();
		try {
			config = loader.load();
		} catch (IOException e) {
			Main.instance().getLog().error("Failed to load config");
			e.printStackTrace();
		}
	}
}
