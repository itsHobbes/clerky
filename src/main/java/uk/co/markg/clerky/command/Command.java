package uk.co.markg.clerky.command;

import java.util.Collections;
import java.util.List;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public interface Command {
  public void execute(SlashCommandInteractionEvent event);

  public default DefaultMemberPermissions definePermissions() {
    return DefaultMemberPermissions.ENABLED;
  }

  public default List<OptionData> defineOptions() {
    return Collections.emptyList();
  }
}
