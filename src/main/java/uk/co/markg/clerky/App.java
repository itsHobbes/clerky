package uk.co.markg.clerky;

import java.awt.Color;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import disparse.discord.jda.Dispatcher;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import uk.co.markg.clerky.listener.VoiceJoinListener;
import uk.co.markg.clerky.listener.VoiceLeaveListener;

public class App {

  public static final String PREFIX = "clerky!";

  public static void main(String[] args) throws Exception {
    Dispatcher.Builder dispatcherBuilder = new Dispatcher.Builder(App.class).prefix(PREFIX)
        .pageLimit(10).withHelpBaseEmbed(() -> new EmbedBuilder().setColor(Color.decode("#eb7701")))
        .description("I manage voice channels")
        .autogenerateReadmeWithNameAndPath("", "COMMANDS.md");

    var builder = Dispatcher.init(JDABuilder.create(System.getenv("CLERKY_TOKEN"), getIntents()),
        dispatcherBuilder.build());
    builder.addEventListeners(new VoiceJoinListener(), new VoiceLeaveListener());
    builder.disableCache(getFlags());
    builder.enableCache(CacheFlag.VOICE_STATE);
    builder.build().awaitReady();
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
    flags.add(CacheFlag.EMOTE);
    return EnumSet.copyOf(flags);
  }

}
