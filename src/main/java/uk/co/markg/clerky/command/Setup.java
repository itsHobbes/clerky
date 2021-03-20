package uk.co.markg.clerky.command;

import java.time.temporal.ChronoUnit;
import java.util.Optional;
import disparse.discord.AbstractPermission;
import disparse.discord.jda.DiscordRequest;
import disparse.parser.dispatch.CooldownScope;
import disparse.parser.reflection.CommandHandler;
import disparse.parser.reflection.Cooldown;
import disparse.parser.reflection.Flag;
import disparse.parser.reflection.MessageStrategy;
import disparse.parser.reflection.ParsedEntity;
import disparse.parser.reflection.Usage;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import uk.co.markg.clerky.data.Config;
import uk.co.markg.clerky.data.ServerConfig;
import uk.co.markg.clerky.listener.ChannelUtility;

public class Setup {

  private static final String USER_ERROR =
      "Max users must be greater than 0 and less than or equal to 99";
  private static final String CHANNEL_ERROR =
      "Max channels must be greater than 0 and less than or equal to 50";

  @ParsedEntity
  static class CategoryRequest {
    @Flag(shortName = 'c', longName = "category",
        description = "The name of the channel category to hold voice channels", required = true)
    String category = "";

    @Flag(longName = "channel",
        description = "The name created channels should use. (Default: Study Room)")
    String channel = "Study Room";

    @Flag(shortName = 'u', longName = "users",
        description = "Max users allowed per channel. (Default: 3)")
    int maxUsers = 3;

    @Flag(shortName = 'n', longName = "channels",
        description = "Max channels allowed per channel. (Default: 10)")
    int maxChannels = 10;
  }

  @Cooldown(amount = 10, unit = ChronoUnit.SECONDS, scope = CooldownScope.USER,
      messageStrategy = MessageStrategy.REACT)
  @CommandHandler(commandName = "setup", description = "Setup voice category",
      perms = AbstractPermission.BAN_MEMBERS)
  @Usage(usage = "-c \"Study Rooms\" -u 3 -n 7 --channel \"Study Room\"",
      description = "Will setup the room management with a maximum of 7 channels with 3 users per channel")
  public void setup(DiscordRequest request, CategoryRequest args) {

    if (args.maxUsers > 99 || args.maxUsers <= 0) {
      request.getEvent().getChannel().sendMessage(USER_ERROR);
      return;
    }

    if (args.maxChannels > 50 || args.maxChannels <= 0) {
      request.getEvent().getChannel().sendMessage(CHANNEL_ERROR).queue();
    }

    execute(request, args);
  }

  private void execute(DiscordRequest request, CategoryRequest args) {
    findCategory(request, args).ifPresentOrElse(
        category -> createChannel(category, args, request.getEvent().getGuild().getMaxBitrate()),
        () -> createCategory(request.getEvent(), args));

    long serverid = request.getEvent().getGuild().getIdLong();

    var config = Config.load();
    config.save(serverid,
        new ServerConfig(args.category, args.channel, args.maxUsers, args.maxChannels));
  }

  private void createCategory(MessageReceivedEvent event, CategoryRequest args) {
    Category c = event.getGuild().createCategory(args.category).setPosition(0).complete();
    createChannel(c, args, event.getGuild().getMaxBitrate());
  }

  private void createChannel(Category category, CategoryRequest args, int maxBitRate) {
    int voiceChannels = category.getVoiceChannels().size();
    if (voiceChannels == 0
        || ChannelUtility.getOccupiedVoiceChannels(category) < voiceChannels - 2) {
      category.createVoiceChannel(args.channel).setUserlimit(args.maxUsers).setBitrate(maxBitRate)
          .queue();
    }
  }

  public Optional<Category> findCategory(DiscordRequest request, CategoryRequest args) {
    var guild = request.getEvent().getGuild();
    var categories = guild.getCategoriesByName(args.category, true);
    for (var category : categories) {
      if (category.getName().equals(args.category)) {
        return Optional.of(category);
      }
    }
    return Optional.empty();
  }

}
