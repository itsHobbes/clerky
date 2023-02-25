package uk.co.markg.clerky.command;

import java.util.Collections;
import java.util.List;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public interface Command {
  public void execute(SlashCommandInteractionEvent event);

  public default List<Permission> getPermissions() {
    return Collections.emptyList();
  }

  public default DefaultMemberPermissions definePermissions() {
    var permissions = getPermissions();
    if (permissions.isEmpty()) {
      return DefaultMemberPermissions.ENABLED;
    } else {
      return DefaultMemberPermissions.enabledFor(getPermissions());
    }
  }

  public default List<OptionData> defineOptions() {
    return Collections.emptyList();
  }
}
