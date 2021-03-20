package uk.co.markg.clerky.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServerConfig {

  @JsonProperty("category_name")
  private String categoryName;

  @JsonProperty("channel_name")
  private String channelName;

  @JsonProperty("max_users")
  private int maxUsers;

  @JsonProperty("max_voice_channels")
  private int maxVoiceChannels;

  public ServerConfig() {
  }

  public ServerConfig(String categoryName, String channelName, int maxUsers, int maxVoiceChannels) {
    this.categoryName = categoryName;
    this.channelName = channelName;
    this.maxUsers = maxUsers;
    this.maxVoiceChannels = maxVoiceChannels;
  }

  /**
   * @return the categoryName
   */
  public String getCategoryName() {
    return categoryName;
  }

  /**
   * @return the channelName
   */
  public String getChannelName() {
    return channelName;
  }

  /**
   * @return the maxUsers
   */
  public int getMaxUsers() {
    return maxUsers;
  }

  /**
   * @return the maxVoiceChannels
   */
  public int getMaxVoiceChannels() {
    return maxVoiceChannels;
  }

}
