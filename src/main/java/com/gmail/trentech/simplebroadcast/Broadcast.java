package com.gmail.trentech.simplebroadcast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.spongepowered.api.Sponge;
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

		boolean isRandom = config.getNode("broadcast", "random").getBoolean();
		
		task = Sponge.getScheduler().createTaskBuilder().interval(config.getNode("broadcast", "minutes").getInt(), TimeUnit.MINUTES).name("broadcast").execute(new Runnable() {

			@Override
			public void run() {
				int size = getBroadcasts().size();

				if (size == 0) {
					return;
				}

				if (played.size() >= size) {
					played.clear();
				}

				int number;
				
				if(isRandom) {
					number = random.nextInt(size);
					
					while (played.contains(number)) {
						number = random.nextInt(size);
					}
				} else {
					number = played.size();
				}

				Main.instance().broadcast(getBroadcasts().get(number));

				played.add(number);
			}

		}).submit(Main.getPlugin());
	}

	public static void init() {
		ConfigurationNode config = ConfigManager.get().getConfig();

		for (String broadcast : config.getNode("broadcast", "messages").getChildrenList().stream().map(ConfigurationNode::getString).collect(Collectors.toList())) {
			getBroadcasts().add(Main.instance().processText(broadcast));
		}

		if (config.getNode("broadcast", "enable").getBoolean()) {
			new Broadcast().start(config);
		}
	}

	public static List<Text> getBroadcasts() {
		return broadcasts;
	}

}
