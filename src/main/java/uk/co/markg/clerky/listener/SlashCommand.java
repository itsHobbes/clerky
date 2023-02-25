package uk.co.markg.clerky.listener;

import java.util.EnumSet;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import uk.co.markg.clerky.command.AddVoiceGroup;

public class SlashCommand extends ListenerAdapter {

  private static final Logger logger = LogManager.getLogger(SlashCommand.class);

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
    switch (event.getName()) {
      case "addVoiceGroup":
        var command = new AddVoiceGroup();
        logger.info("setup triggered");

        if (!hasPermission(event.getMember().getPermissions(), command.getPermissions())) {
          event.reply("You don't have permission to use this command");
        }
        new AddVoiceGroup().execute(event);
        break;
      default:
        break;
    }
  }

  private boolean hasPermission(EnumSet<Permission> memberPermissions,
      List<Permission> commandPermissions) {
    boolean permissionFound = false;
    for (Permission memberPermission : memberPermissions) {
      for (Permission commandPermission : commandPermissions) {
        if (memberPermission.equals(commandPermission)) {
          permissionFound = true;
          break;
        }
      }
      if (permissionFound) {
        break;
      }
    }
    return permissionFound;
  }
}
