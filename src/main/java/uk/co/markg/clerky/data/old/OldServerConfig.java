package uk.co.markg.clerky.data.old;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OldServerConfig {
  @JsonProperty("category_name")
  private String categoryName;

  @JsonProperty("channel_name")
  private String channelName;

  @JsonProperty("max_users")
  private int maxUsers;

  @JsonProperty("max_voice_channels")
  private int maxVoiceChannels;

  public OldServerConfig() {}

  public OldServerConfig(String categoryName, String channelName, int maxUsers, int maxVoiceChannels) {
    this.categoryName = categoryName;
    this.channelName = channelName;
    this.maxUsers = maxUsers;
    this.maxVoiceChannels = maxVoiceChannels;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public String getChannelName() {
    return channelName;
  }

  public int getMaxUsers() {
    return maxUsers;
  }

  public int getMaxVoiceChannels() {
    return maxVoiceChannels;
  }
}
