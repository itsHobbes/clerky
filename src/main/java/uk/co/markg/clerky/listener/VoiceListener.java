package uk.co.markg.clerky.listener;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import uk.co.markg.clerky.data.Config;
import uk.co.markg.clerky.data.VoiceGroupConfig;

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
    var joinedChannel = event.getChannelJoined().asVoiceChannel();
    var parent = joinedChannel.getParentCategory();
    if (parent == null) {
      return;
    }
    var config =
        Config.load().get(event.getGuild().getIdLong()).findVoiceGroupConfig(parent.getName());

    logger.info("Joined channel {}", joinedChannel.getName());

    createChannel(config, parent, joinedChannel, event.getGuild().getMaxBitrate());
  }

  private void left(GuildVoiceUpdateEvent event) {
    logger.info("Channel left");
    var leftChannel = event.getChannelLeft().asVoiceChannel();
    var parent = leftChannel.getParentCategory();
    if (parent == null) {
      return;
    }
    var config =
        Config.load().get(event.getGuild().getIdLong()).findVoiceGroupConfig(parent.getName());
    clearChannel(event.getChannelLeft().asVoiceChannel(), config);
  }

  private void moved(GuildVoiceUpdateEvent event) {
    logger.info("Channel moved");
    var joinedChannel = event.getChannelJoined().asVoiceChannel();
    var parent = joinedChannel.getParentCategory();
    var config =
        Config.load().get(event.getGuild().getIdLong()).findVoiceGroupConfig(parent.getName());
    clearChannel(event.getChannelLeft().asVoiceChannel(), config);
    createChannel(config, parent, joinedChannel, event.getGuild().getMaxBitrate());
  }

  private void createChannel(List<VoiceGroupConfig> config, Category parent,
      VoiceChannel joinedChannel, int maxBitrate) {
    for (VoiceGroupConfig voiceGroupConfig : config) {
      if (!voiceGroupConfig.getCategoryName().equals(parent.getName())) {
        continue;
      }
      if (ChannelUtility.isCreationConditionMet(voiceGroupConfig, joinedChannel, parent)) {
        ChannelUtility.createChannel(parent, voiceGroupConfig, maxBitrate);
      }
    }
  }

  private void clearChannel(VoiceChannel channelLeft, List<VoiceGroupConfig> config) {
    var parent = channelLeft.getParentCategory();
    for (VoiceGroupConfig voiceGroupConfig : config) {
      if (!voiceGroupConfig.getCategoryName().equals(parent.getName())) {
        continue;
      }
      if (isRemovalConditionMet(channelLeft, parent, voiceGroupConfig)) {
        logger.info("Removing channel {}", channelLeft.getId());
        channelLeft.delete().queue();
        return;
      }
    }
    // System.out.println("test");
    // for (var channel : parent.getVoiceChannels()) {
    //   if (!channel.getName().equals(channelLeft.getName())) {
    //     continue;
    //   }

    //   if (channel.getMembers().size() != 0) {
    //     continue;
    //   }

    //   if (Config.load().isStickyChannel(channelLeft.getGuild().getIdLong(), channel.getIdLong())) {
    //     continue;
    //   }
    //   logger.info("Removing channel {}", channel.getId());
    //   channel.delete().queue();
    // }
  }

  private boolean isRemovalConditionMet(VoiceChannel channelLeft, Category parent,
      VoiceGroupConfig config) {
    // parent can be null if a user enters a voice channel that does not have a category.
    if (parent == null) {
      return false;
    }
    int allChannels = ChannelUtility.getValidChannelCount(config.getChannelName(), parent);
    int occupiedChannels = ChannelUtility.getOccupiedVoiceChannels(config.getChannelName(), parent);
    return channelLeft.getName().equals(config.getChannelName())
        && channelLeft.getMembers().isEmpty() && allChannels > 1
        && parent.getName().equals(config.getCategoryName()) && occupiedChannels <= allChannels - 2;
        // && !Config.load().isStickyChannel(channelLeft.getGuild().getIdLong(), channelLeft.getIdLong());
  }

}
