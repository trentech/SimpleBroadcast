package com.gmail.trentech.simplebroadcast.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList.Builder;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.gmail.trentech.simplebroadcast.Main;
import com.gmail.trentech.simpletags.tags.SingleTag;
import com.gmail.trentech.simpletags.utils.Help;

public class CMDTagBroadcast implements CommandExecutor {

	public static CommandSpec cmd = CommandSpec.builder().permission("simpletags.cmd.tag.broadcast").arguments(GenericArguments.optional(GenericArguments.string(Text.of("tag")))).executor(new CMDTagBroadcast()).build();

	public CMDTagBroadcast() {
		Help help = new Help("broadcast", "broadcast", " View and edit broadcast tags");
		help.setSyntax(" /tag broadcast <tag>\n /t g <tag>");
		help.setExample(" /tag broadcast\n /tag broadcast &e[broadcast]\n /tag broadcast reset");
		help.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Optional<SingleTag> optionalTag = SingleTag.get(Main.getPlugin().getId(), "broadcast");

		if (!args.hasAny("tag")) {
			List<Text> list = new ArrayList<>();

			if (optionalTag.isPresent()) {
				list.add(Text.of(TextColors.GREEN, "Current Tag: ", TextColors.RESET, optionalTag.get().getTag()));
			} else {
				list.add(Text.of(TextColors.GREEN, "Current Tag: ", TextColors.RED, "NONE"));
			}

			list.add(Text.of(TextColors.GREEN, "Update Tag: ", TextColors.YELLOW, "/tag broadcast <tag>"));

			if (src instanceof Player) {
				Builder pages = Main.getGame().getServiceManager().provide(PaginationService.class).get().builder();

				pages.title(Text.builder().color(TextColors.DARK_GREEN).append(Text.of(TextColors.GREEN, "Channel")).build());

				pages.contents(list);

				pages.sendTo(src);
			} else {
				for (Text text : list) {
					src.sendMessage(text);
				}
			}

			return CommandResult.success();
		}
		String tag = args.<String> getOne("tag").get();

		if (tag.equalsIgnoreCase("reset")) {
			if (optionalTag.isPresent()) {
				optionalTag.get().setTag(null);
			}
			src.sendMessage(Text.of(TextColors.DARK_GREEN, "Tag reset"));

			return CommandResult.success();
		}

		if (optionalTag.isPresent()) {
			SingleTag broadcastTag = optionalTag.get();
			broadcastTag.setTag(tag);
		} else {
			SingleTag.create(Main.getPlugin().getId(), "broadcast", tag);
		}

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Tag changed to ", TextSerializers.FORMATTING_CODE.deserialize(tag)));

		return CommandResult.success();
	}

}
