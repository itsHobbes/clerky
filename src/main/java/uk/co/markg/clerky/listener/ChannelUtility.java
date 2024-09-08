package uk.co.markg.clerky.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import uk.co.markg.clerky.data.VoiceGroupConfig;

public class ChannelUtility {

  private static final Logger logger = LogManager.getLogger(ChannelUtility.class);

  public static int getOccupiedVoiceChannels(String channelName, Category parent) {
    int sum = 0;
    for (var channel : parent.getVoiceChannels()) {
      if (channel.getName().equals(channelName) && channel.getMembers().size() > 0) {
        sum++;
      }
    }
    logger.info(sum);
    return sum;
  }

  public static boolean voiceChannelExists(String channelName, Category parent) {
    for (var channel : parent.getVoiceChannels()) {
      if (channel.getName().equals(channelName)) {
        return true;
      }
    }
    return false;
  }

  public static int getValidChannelCount(String channelName, Category parent) {
    int sum = 0;
    for (var channel : parent.getVoiceChannels()) {
      if (channel.getName().equals(channelName)) {
        sum++;
      }
    }
    return sum;
  }

  public static boolean isCreationConditionMet(VoiceGroupConfig config, VoiceChannel joinedChannel,
      Category parent) {
    int occupiedChannels = ChannelUtility.getOccupiedVoiceChannels(config.getChannelName(), parent);
    int totalExistingValidChannels = getValidChannelCount(config.getChannelName(), parent);
    return joinedChannel.getName().equals(config.getChannelName())
        && parent.getName().equals(config.getCategoryName())
        && totalExistingValidChannels < config.getMaxVoiceChannels()
        && totalExistingValidChannels - occupiedChannels == 0;
  }

  public static void createChannel(Category parent, VoiceGroupConfig config, int bitRate) {
    createChannel(parent, config.getChannelName(), config.getMaxUsers(), bitRate);
  }

  public static void createChannel(Category parent, String channelName, int maxUsers, int bitRate) {
    logger.info("Creating channel {}", channelName);
    parent.createVoiceChannel(channelName).setUserlimit(maxUsers).setBitrate(bitRate)
        .syncPermissionOverrides().queue();
  }

}
