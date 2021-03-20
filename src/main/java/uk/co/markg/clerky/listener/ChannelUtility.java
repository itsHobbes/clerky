package uk.co.markg.clerky.listener;

import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.VoiceChannel;
import uk.co.markg.clerky.data.ServerConfig;

public class ChannelUtility {

  public static int getOccupiedVoiceChannels(Category parent) {
    parent.getVoiceChannels();
    int sum = 0;
    for (var channel : parent.getVoiceChannels()) {
      if (channel.getMembers().size() > 0) {
        sum++;
      }
    }
    return sum;
  }

  public static boolean isCreationConditionMet(ServerConfig config, VoiceChannel joinedChannel,
      Category parent) {
    return joinedChannel.getName().equals(config.getChannelName())
        && parent.getName().equals(config.getCategoryName())
        && parent.getVoiceChannels().size() < config.getMaxVoiceChannels()
        && ChannelUtility.getOccupiedVoiceChannels(parent) >= parent.getVoiceChannels().size();
  }

}
