package com.gmail.trentech.simplebroadcast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import com.gmail.trentech.simplebroadcast.utils.ConfigManager;

import ninja.leaping.configurate.ConfigurationNode;

public class Broadcast {

	private static List<Text> broadcasts = new ArrayList<>();
	private static ThreadLocalRandom random = ThreadLocalRandom.current();

	public Task task;

	public void start(ConfigurationNode config) {
		List<Integer> played = new ArrayList<>();

		int minutes = config.getNode("broadcast", "minutes").getInt();

		task = Main.getGame().getScheduler().createTaskBuilder().interval(minutes, TimeUnit.MINUTES).name("broadcast").execute(new Runnable() {

			@Override
			public void run() {
				int size = getBroadcasts().size();

				if (size == 0) {
					return;
				}

				int number = random.nextInt(size);

				if (played.size() >= size) {
					played.clear();
				}

				while (played.contains(number)) {
					number = random.nextInt(size);
				}

				Main.broadcast(getBroadcasts().get(number));

				played.add(number);
			}

		}).submit(Main.getPlugin());
	}

	public static void init() {
		ConfigurationNode config = new ConfigManager().getConfig();

		for (String broadcast : config.getNode("broadcast", "messages").getChildrenList().stream().map(ConfigurationNode::getString).collect(Collectors.toList())) {
			getBroadcasts().add(Main.processText(broadcast));
		}

		if (config.getNode("broadcast", "enable").getBoolean()) {
			new Broadcast().start(config);
		}
	}

	public static List<Text> getBroadcasts() {
		return broadcasts;
	}

}
