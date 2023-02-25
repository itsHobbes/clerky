package uk.co.markg.clerky.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import uk.co.markg.clerky.command.Setup;

public class SlashCommand extends ListenerAdapter {

  private static final Logger logger = LogManager.getLogger(SlashCommand.class);

  @Override
  public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
    switch (event.getName()) {
      case "setup":
        logger.info("setup triggered");
        if (!event.getMember().hasPermission(Permission.BAN_MEMBERS)) {
          event.reply("You don't have permission to use this command");
        }
        new Setup().execute(event);
        break;
      default:
        break;
    }
  }
}
