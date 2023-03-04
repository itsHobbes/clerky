package uk.co.markg.clerky.command;

import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import uk.co.markg.clerky.data.Config;

@CommandInfo(name = "removevoicegroups", description = "List all voice groups.")
public class RemoveVoiceGroup implements Command {

  private static final String ID_ARG = "id";

  @Override
  public void execute(SlashCommandInteractionEvent event) {
    try {
      var id = Long.parseLong(event.getOption(ID_ARG, OptionMapping::getAsString));
      var config = Config.load();
      var serverConfig = config.get(event.getGuild().getIdLong());
      var voiceGroup = serverConfig.findById(id);
      if (voiceGroup == null) {
        event.reply("Invalid ID").queue();
        return;
      }
      serverConfig.removeById(id);
      config.save();
      event.reply("Voice Group removed").queue();
    } catch (NumberFormatException e) {
      event.reply("Invalid ID").queue();
    }
  }

  @Override
  public List<Permission> getPermissions() {
    return List.of(Permission.BAN_MEMBERS);
  }

  @Override
  public List<OptionData> defineOptions() {
    List<OptionData> options = new ArrayList<>();
    options.add(new OptionData(OptionType.STRING, ID_ARG,
        "The category name as a string to hold voice channels", true));
    return options;
  }

}
