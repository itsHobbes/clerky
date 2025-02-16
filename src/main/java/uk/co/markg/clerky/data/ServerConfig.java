package uk.co.markg.clerky.data;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ServerConfig {

  @JsonProperty("id")
  private long serverId;

  @JsonProperty("voice_config")
  private List<VoiceGroupConfig> voiceConfig;

  @JsonProperty("sticky_channels")
  private List<Long> stickyChannels;

  public ServerConfig() {}

  public ServerConfig(long serverId, List<VoiceGroupConfig> voiceConfig, List<Long> channels) {
    this.serverId = serverId;
    this.voiceConfig = voiceConfig;
    this.stickyChannels = channels;
  }

  /**
   * @return the serverId
   */
  public long getServerId() {
    return serverId;
  }

  /**
   * @return the voiceConfig
   */
  public List<VoiceGroupConfig> getVoiceConfig() {
    return voiceConfig;
  }

  /**
   * @return the stickyChannels
   */
  public List<Long> getStickyChannels() {
    return stickyChannels;
  }

  public void removeVoiceConfig(long id) {
    var group = voiceConfig.stream().filter(vc -> vc.getId() == id).findFirst().orElse(null);
    voiceConfig.remove(group);
  }

  public VoiceGroupConfig findVoiceConfig(long id) {
    return voiceConfig.stream().filter(vc -> vc.getId() == id).findFirst().orElse(null);
  }

  public List<VoiceGroupConfig> findVoiceGroupConfig(String categoryName) {
    return voiceConfig.stream().filter(vc -> vc.getCategoryName().equals(categoryName)).toList();
  }

  public void addVoiceGroupConfig(VoiceGroupConfig config) {
    if (voiceConfig == null) {
      voiceConfig = new ArrayList<>();
    }
    voiceConfig.add(config);
  }

  public void addStickyChannel(long channelId) {
    boolean found = false;
    for (long stickyChannel : stickyChannels) {
      if (channelId == stickyChannel) {
        found = true;
        break;
      }
    }
    if (found) {
      return;
    }
    stickyChannels.add(channelId);
  }

  public void removeStickyChannel(long channelId) {
    stickyChannels.remove(channelId);
  }

  public boolean isStickyChannel(long channelId) {
    for (long stickyChannel : stickyChannels) {
      if (channelId == stickyChannel) {
        return true;
      }
    }
    return false;
  }
}
