package uk.co.markg.clerky.command;

import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import uk.co.markg.clerky.data.Config;

@CommandInfo(name = "size", description = "Set voice group size")
public class AlterVoiceGroupSize implements Command {

  private static final String SIZE_ARG = "size";

  @Override
  public List<OptionData> defineOptions() {
    List<OptionData> options = new ArrayList<>();
    options.add(new OptionData(OptionType.INTEGER, SIZE_ARG,
        "New size for the voice group", true));
    return options;
  }

  @Override
  public void execute(SlashCommandInteractionEvent event) {
    event.deferReply(true).queue();
    if (event.getChannelType() != ChannelType.VOICE) {
      event.getHook().editOriginal("This channel is not a voice text channel.").queue();
    }
    var config = Config.load();
    var voiceGroups = config.getVoiceGroups(event.getGuild().getIdLong());
    var channel = event.getChannel().asVoiceChannel();
    var parent = channel.getParentCategory();
    if (parent == null) {
      event.getHook().editOriginal("This channel is not managed by clerky.").queue();
      return;
    }
    var name = parent.getName();

    for (var voiceGroupConfig : voiceGroups) {
      if (!name.equals(voiceGroupConfig.getCategoryName())) {
        continue;
      }

      if (!channel.getName().equals(voiceGroupConfig.getChannelName())) {
        event.getHook().editOriginal("This channel is not managed by clerky.").queue();
        return;
      }

      var channelMembers = channel.getMembers();

      boolean isFound = false;
      for (var member : channelMembers) {
        if (member.getIdLong() == event.getMember().getIdLong()) {
          isFound = true;
          break;
        }
      }

      if (!isFound) {
        event.getHook().editOriginal("You must be connected to this voice channel to edit member size.").queue();
        return;
      }

      if (!voiceGroupConfig.isAdjustableSize()) {
        event.getHook().editOriginal("Channel size cannot be adjusted.").queue();
        return;
      }

      var newSize = event.getOption(SIZE_ARG, OptionMapping::getAsInt);
      if (newSize < channel.getMembers().size()) {
        event.getHook().editOriginal("Cannot set size to less than current channel member count.").queue();
        return;
      }

      channel.getManager().setUserLimit(newSize).queue();
      event.getHook().editOriginal("Channel size updated!").queue();
      return;
    }

    event.getHook().editOriginal("Something went wrong").queue();

  }
}
