package uk.co.markg.clerky.data.old;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OldConfig {

  @JsonProperty("server_configs")
  private Map<Long, OldServerConfig> serverConfigs;

  public OldConfig() {
    serverConfigs = new HashMap<>();
  }

  public Map<Long, OldServerConfig> getAll() {
    return serverConfigs;
  }
}
