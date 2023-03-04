package uk.co.markg.clerky.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VoiceGroupConfig {

  @JsonProperty("id")
  private long id;

  @JsonProperty("category_name")
  private String categoryName;

  @JsonProperty("channel_name")
  private String channelName;

  @JsonProperty("max_users")
  private int maxUsers;

  @JsonProperty("max_voice_channels")
  private int maxVoiceChannels;

  public VoiceGroupConfig() {}

  public VoiceGroupConfig(long id, String categoryName, String channelName, int maxUsers,
      int maxVoiceChannels) {
    this.id = id;
    this.categoryName = categoryName;
    this.channelName = channelName;
    this.maxUsers = maxUsers;
    this.maxVoiceChannels = maxVoiceChannels;
  }

  public VoiceGroupConfig(String categoryName, String channelName, int maxUsers,
      int maxVoiceChannels) {
    this.id = System.currentTimeMillis();
    this.categoryName = categoryName;
    this.channelName = channelName;
    this.maxUsers = maxUsers;
    this.maxVoiceChannels = maxVoiceChannels;
  }


  /**
   * @return the id
   */
  public long getId() {
    return id;
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
