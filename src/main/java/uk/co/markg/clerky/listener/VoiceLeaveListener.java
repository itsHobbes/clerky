package uk.co.markg.clerky.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import uk.co.markg.clerky.data.Config;
import uk.co.markg.clerky.data.ServerConfig;

public class VoiceLeaveListener extends ListenerAdapter {

  private static final Logger logger = LogManager.getLogger(VoiceLeaveListener.class);

  @Override
  public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
    logger.info("Channel left");
    var config = Config.load().get(event.getGuild().getIdLong());
    clearChannel(event.getChannelLeft(), config);
  }

  @Override
  public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
    logger.info("Channel moved");
    var config = Config.load().get(event.getGuild().getIdLong());
    clearChannel(event.getChannelLeft(), config);
    var joinedChannel = event.getChannelJoined();
    var parent = joinedChannel.getParent();
    if (ChannelUtility.isCreationConditionMet(config, joinedChannel, parent)) {
      parent.createVoiceChannel(config.getChannelName()).setUserlimit(config.getMaxUsers()).queue();
    }
  }

  private void clearChannel(VoiceChannel channelLeft, ServerConfig config) {
    var parent = channelLeft.getParent();
    if (isRemovalConditionMet(channelLeft, parent, config)) {
      channelLeft.delete().queue();
    }
  }

  private boolean isRemovalConditionMet(VoiceChannel channelLeft, Category parent,
      ServerConfig config) {
    if (parent == null) {
      return false;
    }
    int allChannels = ChannelUtility.getValidChannelCount(config.getChannelName(), parent);
    logger.info(ChannelUtility.getOccupiedVoiceChannels(config.getChannelName(), parent));
    return channelLeft.getName().equals(config.getChannelName())
        && channelLeft.getMembers().isEmpty() && allChannels > 1
        && parent.getName().equals(config.getCategoryName())
        && ChannelUtility.getOccupiedVoiceChannels(config.getChannelName(), parent) <= allChannels - 2;
  }

}
