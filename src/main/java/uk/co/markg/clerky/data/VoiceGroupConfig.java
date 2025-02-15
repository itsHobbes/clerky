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

  @JsonProperty("adjustable_size")
  private boolean adjustableSize;

  public VoiceGroupConfig() {}

  public VoiceGroupConfig(long id, String categoryName, String channelName, int maxUsers,
      int maxVoiceChannels, boolean adjustableSize) {
    this.id = id;
    this.categoryName = categoryName;
    this.channelName = channelName;
    this.maxUsers = maxUsers;
    this.maxVoiceChannels = maxVoiceChannels;
    this.adjustableSize = adjustableSize;
  }

  public VoiceGroupConfig(String categoryName, String channelName, int maxUsers,
      int maxVoiceChannels, boolean adjustableSize) {
    this.id = System.currentTimeMillis();
    this.categoryName = categoryName;
    this.channelName = channelName;
    this.maxUsers = maxUsers;
    this.maxVoiceChannels = maxVoiceChannels;
    this.adjustableSize = adjustableSize;
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

  public boolean isAdjustableSize() {
    return adjustableSize;
  }

  /**
   * @param maxUsers the maxUsers to set
   */
  public void setMaxUsers(int maxUsers) {
    this.maxUsers = maxUsers;
  }

}
