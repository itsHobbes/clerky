package uk.co.markg.clerky.command;

import java.util.Optional;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import uk.co.markg.clerky.data.Config;
import uk.co.markg.clerky.data.ServerConfig;
import uk.co.markg.clerky.listener.ChannelUtility;

public class Setup {

  private static final String USER_ERROR =
      "Max users must be greater than 0 and less than or equal to 99";
  private static final String CHANNEL_ERROR =
      "Max channels must be greater than 0 and less than or equal to 50";

  public static void execute(SlashCommandInteractionEvent event) {

    var categoryName = event.getOption("category", OptionMapping::getAsString);
    var channel = event.getOption("channel", OptionMapping::getAsString);
    var maxUsers = event.getOption("maxusers", OptionMapping::getAsInt);
    var maxChannels = event.getOption("maxchannels", OptionMapping::getAsInt);

    if (maxUsers > 99 || maxUsers <= 0) {
      event.reply(USER_ERROR).queue();
      return;
    }

    if (maxChannels > 50 || maxChannels <= 0) {
      event.reply(CHANNEL_ERROR).queue();
      return;
    }

    event.deferReply().queue();

    findCategory(event, categoryName).ifPresentOrElse(
        category -> createChannel(category, channel, maxUsers, event.getGuild().getMaxBitrate()),
        () -> createCategory(event, categoryName, channel, maxUsers));

    long serverid = event.getGuild().getIdLong();

    var config = Config.load();
    config.save(serverid, new ServerConfig(categoryName, channel, maxUsers, maxChannels));

    event.getHook().editOriginal("Config set").queue();
  }

  private static void createCategory(SlashCommandInteractionEvent event, String categoryName,
      String channel, int maxUsers) {
    Category c = event.getGuild().createCategory(categoryName).setPosition(0).complete();
    createChannel(c, channel, maxUsers, event.getGuild().getMaxBitrate());
  }

  private static void createChannel(Category category, String channel, int maxUsers,
      int maxBitRate) {
    if (!ChannelUtility.voiceChannelExists(channel, category)) {
      ChannelUtility.createChannel(category, channel, maxUsers, maxBitRate);
    }
  }

  public static Optional<Category> findCategory(SlashCommandInteractionEvent event,
      String categoryName) {
    var guild = event.getGuild();
    var categories = guild.getCategoriesByName(categoryName, true);
    for (var cat : categories) {
      if (cat.getName().equals(categoryName)) {
        return Optional.of(cat);
      }
    }
    return Optional.empty();
  }

}
