package uk.co.markg.clerky;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import uk.co.markg.clerky.listener.MentionListener;
import uk.co.markg.clerky.listener.SlashCommand;
import uk.co.markg.clerky.listener.VoiceListener;

public class App {

  public static final String PREFIX = "clerky!";

  public static void main(String[] args) throws Exception {

    var builder = JDABuilder.create(System.getenv("CLERKY_TOKEN"), getIntents());
    builder.addEventListeners(new VoiceListener(), new MentionListener(), new SlashCommand());
    builder.disableCache(getFlags());
    builder.enableCache(CacheFlag.VOICE_STATE);
    var jda = builder.build();
    jda.awaitReady();
    jda.updateCommands().addCommands(Commands.slash("setup", "Clerky Setup")
        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS))
        .setGuildOnly(true)
        .addOption(OptionType.STRING, "category", "The category name as a string to hold voice channels", true)
        .addOption(OptionType.STRING, "channel", "The name of the voice channels", true)
        .addOption(OptionType.INTEGER, "maxusers", "The max number of users per voice channel",
            true)
        .addOption(OptionType.INTEGER, "maxchannels", "The max number of channels", true)).queue();
  }

  private static List<GatewayIntent> getIntents() {
    List<GatewayIntent> intents = new ArrayList<>();
    intents.add(GatewayIntent.GUILD_MESSAGES);
    intents.add(GatewayIntent.GUILD_VOICE_STATES);
    return intents;
  }

  private static EnumSet<CacheFlag> getFlags() {
    List<CacheFlag> flags = new ArrayList<>();
    flags.add(CacheFlag.ACTIVITY);
    flags.add(CacheFlag.CLIENT_STATUS);
    flags.add(CacheFlag.EMOJI);
    return EnumSet.copyOf(flags);
  }

}
