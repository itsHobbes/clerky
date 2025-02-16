package uk.co.markg.clerky.listener;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import uk.co.markg.clerky.App;
import uk.co.markg.clerky.command.AddVoiceGroup;
import uk.co.markg.clerky.command.AlterVoiceGroupSize;
import uk.co.markg.clerky.command.ListVoiceGroup;
import uk.co.markg.clerky.command.RemoveVoiceGroup;
import uk.co.markg.clerky.command.StickyChannel;

public class SlashCommand extends ListenerAdapter {

  private static final Logger logger = LogManager.getLogger(SlashCommand.class);

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
    logger.info(event.getName());
    var command = switch (event.getName().replace(App.PREFIX, "")) {
      case "addvoicegroup" -> new AddVoiceGroup();
      case "listvoicegroups" -> new ListVoiceGroup();
      case "removevoicegroups" -> new RemoveVoiceGroup();
      case "size" -> new AlterVoiceGroupSize();
      case "sticky" -> new StickyChannel();
      default -> null;
    };

    if (command == null) {
      logger.warn("{} triggered with no execution available", event.getName());
      return;
    }

    if (!hasPermission(event, command.getPermissions())) {
      event.reply("You don't have permission to use this command").queue();
      return;
    }

    logger.info("{} command triggered", command.getClass().getCanonicalName());
    command.execute(event);
  }

  private boolean hasPermission(SlashCommandInteractionEvent event,
      List<Permission> commandPermissions) {
    if (event.getMember() == null) {
      return true;
    }
    if (commandPermissions.isEmpty()) {
      return true;
    }
    var memberPermissions = event.getMember().getPermissions();
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
