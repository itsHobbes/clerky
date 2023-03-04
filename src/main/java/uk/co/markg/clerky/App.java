package uk.co.markg.clerky;

import static org.reflections.scanners.Scanners.SubTypes;
import static org.reflections.scanners.Scanners.TypesAnnotated;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.reflections.Reflections;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import uk.co.markg.clerky.command.Command;
import uk.co.markg.clerky.command.CommandInfo;
import uk.co.markg.clerky.data.Config;
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

    var reflections = new Reflections("uk.co.markg.clerky");
    Set<Class<?>> annotated =
        reflections.get(SubTypes.of(TypesAnnotated.with(CommandInfo.class)).asClass());

    List<CommandData> commands = findSlashCommands(annotated);
    jda.updateCommands().addCommands(commands).queue();
    createConfig(jda);
  }

  private static void createConfig(JDA jda) {
    var config = Config.load();
    jda.getGuilds().forEach(server -> {
      var id = server.getIdLong();
      if (config.get(id) == null) {
        config.addServerConfig(id);
      }
    });
  }

  private static List<CommandData> findSlashCommands(Set<Class<?>> annotated) {
    List<CommandData> commands = new ArrayList<>();
    for (Class<?> clazz : annotated) {
      try {
        var commandClass = clazz.asSubclass(Command.class);
        var commandData = commandClass.getAnnotation(CommandInfo.class);
        Command command = commandClass.getDeclaredConstructor().newInstance();

        var commandBuilder = Commands.slash(commandData.name(), commandData.description())
            .setGuildOnly(commandData.guildOnly())
            .setDefaultPermissions(command.definePermissions());

        if (!command.defineOptions().isEmpty()) {
          commandBuilder.addOptions(command.defineOptions());
        }
        commands.add(commandBuilder);
        System.out.printf("Registered %s%n", commandData);
      } catch (ReflectiveOperationException e) {
        System.err.printf("%s does not implement %s%n", clazz, Command.class);
      }
    }
    return commands;
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
