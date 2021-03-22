package uk.co.markg.clerky.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import uk.co.markg.clerky.data.Config;

public class VoiceJoinListener extends ListenerAdapter {

  private static final Logger logger = LogManager.getLogger(VoiceJoinListener.class);

  @Override
  public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
    var config = Config.load().get(event.getGuild().getIdLong());
    var joinedChannel = event.getChannelJoined();
    var parent = joinedChannel.getParent();

    logger.info("Joined channel {}", joinedChannel.getName());

    if (ChannelUtility.isCreationConditionMet(config, joinedChannel, parent)) {
      ChannelUtility.createChannel(parent, config, event.getGuild().getMaxBitrate());
    }
  }



}
