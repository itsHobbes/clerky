package uk.co.markg.clerky.command;

import java.util.List;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import uk.co.markg.clerky.data.Config;

@CommandInfo(name = "sticky", description = "Stickify a channel so it will not be removed automatically.")
public class StickyChannel implements Command {

  @Override
  public List<Permission> getPermissions() {
    return List.of(Permission.BAN_MEMBERS);
  }

  @Override
  public void execute(SlashCommandInteractionEvent event) {
    event.deferReply(true).queue();

    var channel = event.getChannel().asVoiceChannel();
    var parent = channel.getParentCategory();
    if (parent == null) {
      event.getHook().editOriginal("This channel is not managed by clerky.").queue();
      return;
    }


    var config = Config.load();
    var voiceGroups = config.getVoiceGroups(event.getGuild().getIdLong());

    var name = parent.getName();
    for (var voiceGroupConfig : voiceGroups) {
      if (!name.equals(voiceGroupConfig.getCategoryName())) {
        continue;
      }

      if (!channel.getName().equals(voiceGroupConfig.getChannelName())) {
        event.getHook().editOriginal("This channel is not managed by clerky.").queue();
        return;
      }

      long serverId = event.getGuild().getIdLong();
      boolean isSticky = config.isStickyChannel(serverId, channel.getIdLong());
      if (isSticky) {
        config.removeStickyChannel(serverId, channel.getIdLong());
        event.getHook().editOriginal("This channel is no longer sticky and will be deleted automatically.").queue();
      } else {
        config.addStickyChannel(serverId, channel.getIdLong());
        event.getHook().editOriginal("This channel is now sticky and will not be deleted automatically.").queue();
      }
      return;
    }

    event.getHook().editOriginal("Something went wrong").queue();
  }
}
