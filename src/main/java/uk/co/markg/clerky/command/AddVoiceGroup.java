package uk.co.markg.clerky.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import uk.co.markg.clerky.data.Config;
import uk.co.markg.clerky.data.VoiceGroupConfig;
import uk.co.markg.clerky.listener.ChannelUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@CommandInfo(name = "addvoicegroup", description = "Add a voice group")
public class AddVoiceGroup implements Command {

  private static final Logger logger = LogManager.getLogger(ChannelUtility.class);

  private static final String USER_ERROR =
      "Max users must be greater than 0 and less than or equal to 99";
  private static final String CHANNEL_ERROR =
      "Max channels must be greater than 0 and less than or equal to 50";
  private static final String CATEGORY_ARG = "category";
  private static final String CHANNEL_ARG = "channel";
  private static final String MAX_USERS_ARG = "maxusers";
  private static final String MAX_CHANNELS_ARG = "maxchannels";
  private static final String ADJUSTABLE_ARG = "adjustablesize";

  @Override
  public List<Permission> getPermissions() {
    return List.of(Permission.BAN_MEMBERS);
  }

  @Override
  public List<OptionData> defineOptions() {
    List<OptionData> options = new ArrayList<>();
    options.add(new OptionData(OptionType.STRING, CATEGORY_ARG,
        "The category name as a string to hold voice channels", true));
    options.add(
        new OptionData(OptionType.STRING, CHANNEL_ARG, "The name of the voice channels", true));
    options.add(new OptionData(OptionType.STRING, MAX_USERS_ARG,
        "The max number of users per voice channel", true));
    options.add(
        new OptionData(OptionType.STRING, MAX_CHANNELS_ARG, "The max number of channels", true));
    options.add(
        new OptionData(OptionType.BOOLEAN, ADJUSTABLE_ARG, "Whether users can adjust the size of the channel", false));
    return options;
  }

  @Override
  public void execute(SlashCommandInteractionEvent event) {
    var categoryName = event.getOption(CATEGORY_ARG, OptionMapping::getAsString);
    var channel = event.getOption(CHANNEL_ARG, OptionMapping::getAsString);
    var maxUsers = event.getOption(MAX_USERS_ARG, OptionMapping::getAsInt);
    var maxChannels = event.getOption(MAX_CHANNELS_ARG, OptionMapping::getAsInt);
    var adjustable = event.getOption(ADJUSTABLE_ARG, OptionMapping::getAsBoolean);

    adjustable = adjustable == null ? false : adjustable;

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
    config.addVoiceGroup(serverid,
        new VoiceGroupConfig(categoryName, channel, maxUsers, maxChannels, adjustable));

    event.getHook().editOriginal("Config set").queue();
  }

  private void createCategory(SlashCommandInteractionEvent event, String categoryName,
      String channel, int maxUsers) {
    Category c = event.getGuild().createCategory(categoryName).setPosition(0).complete();
    createChannel(c, channel, maxUsers, event.getGuild().getMaxBitrate());
  }

  private void createChannel(Category category, String channel, int maxUsers, int maxBitRate) {
    if (!ChannelUtility.voiceChannelExists(channel, category)) {
      ChannelUtility.createChannel(category, channel, maxUsers, maxBitRate);
    }
  }

  public Optional<Category> findCategory(SlashCommandInteractionEvent event, String categoryName) {
    var guild = event.getGuild();
    var categories = guild.getCategoriesByName(categoryName, true);
    for (var cat : categories) {
      if (cat.getName().equals(categoryName)) {
        return Optional.of(cat);
      }
    }
    logger.info("Category not found");
    return Optional.empty();
  }
}
