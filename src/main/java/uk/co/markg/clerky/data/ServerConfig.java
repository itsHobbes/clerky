package uk.co.markg.clerky.data;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ServerConfig {

  @JsonProperty("id")
  private long serverId;

  @JsonProperty("voice_config")
  private List<VoiceGroupConfig> voiceConfig;

  public ServerConfig() {}

  public ServerConfig(long serverId, List<VoiceGroupConfig> voiceConfig) {
    this.serverId = serverId;
    this.voiceConfig = voiceConfig;
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

  public List<VoiceGroupConfig> findVoiceGroupConfigByCategory(String categoryName) {
    return voiceConfig.stream().filter(vc -> vc.getCategoryName().equals(categoryName)).toList();
  }

  public void addVoiceGroupConfig(VoiceGroupConfig config) {
    if (voiceConfig == null) {
      voiceConfig = new ArrayList<>();
    }
    voiceConfig.add(config);
  }
}
