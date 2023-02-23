package uk.co.markg.clerky.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import uk.co.markg.clerky.data.Config;
import uk.co.markg.clerky.data.ServerConfig;

public class VoiceListener extends ListenerAdapter {

  private static final Logger logger = LogManager.getLogger(VoiceListener.class);

  @Override
  public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {

    if (event.getChannelJoined() != null && event.getChannelLeft() == null) {
      joined(event);
    } else if (event.getChannelLeft() != null && event.getChannelJoined() == null) {
      left(event);
    } else if (event.getChannelJoined() != null && event.getChannelLeft() != null) {
      moved(event);
    }
  }

  private void joined(GuildVoiceUpdateEvent event) {
    var config = Config.load().get(event.getGuild().getIdLong());
    var joinedChannel = event.getChannelJoined().asVoiceChannel();
    var parent = joinedChannel.getParentCategory();

    logger.info("Joined channel {}", joinedChannel.getName());

    if (ChannelUtility.isCreationConditionMet(config, joinedChannel, parent)) {
      ChannelUtility.createChannel(parent, config, event.getGuild().getMaxBitrate());
    }
  }

  private void left(GuildVoiceUpdateEvent event) {
    logger.info("Channel left");
    var config = Config.load().get(event.getGuild().getIdLong());
    clearChannel(event.getChannelLeft().asVoiceChannel(), config);
  }

  private void moved(GuildVoiceUpdateEvent event) {
    logger.info("Channel moved");
    var config = Config.load().get(event.getGuild().getIdLong());
    clearChannel(event.getChannelLeft().asVoiceChannel(), config);
    var joinedChannel = event.getChannelJoined().asVoiceChannel();
    var parent = joinedChannel.getParentCategory();
    if (ChannelUtility.isCreationConditionMet(config, joinedChannel, parent)) {
      ChannelUtility.createChannel(parent, config, event.getGuild().getMaxBitrate());
    }
  }

  private void clearChannel(VoiceChannel channelLeft, ServerConfig config) {
    var parent = channelLeft.getParentCategory();
    if (isRemovalConditionMet(channelLeft, parent, config)) {
      channelLeft.delete().queue();
    }
  }

  private boolean isRemovalConditionMet(VoiceChannel channelLeft, Category parent,
      ServerConfig config) {
    // parent can be null if a user enters a voice channel that does not have a category.
    if (parent == null) {
      return false;
    }
    int allChannels = ChannelUtility.getValidChannelCount(config.getChannelName(), parent);
    int occupiedChannels = ChannelUtility.getOccupiedVoiceChannels(config.getChannelName(), parent);
    return channelLeft.getName().equals(config.getChannelName())
        && channelLeft.getMembers().isEmpty() && allChannels > 1
        && parent.getName().equals(config.getCategoryName()) && occupiedChannels <= allChannels - 2;
  }

}
