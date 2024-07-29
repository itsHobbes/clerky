package uk.co.markg.clerky.command;

import java.util.List;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import uk.co.markg.clerky.data.Config;

@CommandInfo(name = "clerky-listvoicegroups", description = "List all voice groups.")
public class ListVoiceGroup implements Command {

  @Override
  public void execute(SlashCommandInteractionEvent event) {
    event.deferReply().queue();
    var id = event.getGuild().getIdLong();
    var sb = new StringBuilder();
    var voiceConfigs = Config.load().getVoiceGroups(id);
    if (voiceConfigs.isEmpty()) {
      event.getHook().editOriginal("There are no voice groups.").queue();
      return;
    }
    voiceConfigs.forEach(config -> {
      sb.append("**ID:** " + config.getId()).append(System.lineSeparator());
      sb.append("**Category:** " + config.getCategoryName()).append(System.lineSeparator());
      sb.append("**Channel:** " + config.getChannelName()).append(System.lineSeparator());
      sb.append("**Max Users:** " + config.getMaxUsers()).append(System.lineSeparator());
      sb.append("**Max Channels:** " + config.getMaxVoiceChannels()).append(System.lineSeparator());
      sb.append(System.lineSeparator());
    });
    event.getHook().editOriginal(sb.toString()).queue();
  }

  @Override
  public List<Permission> getPermissions() {
    return List.of(Permission.BAN_MEMBERS);
  }
}
