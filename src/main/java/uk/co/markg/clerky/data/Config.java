package uk.co.markg.clerky.data;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Config {

  private static final String FILENAME = "config.json";

  @JsonProperty("server_configs")
  private Map<Long, ServerConfig> serverConfigs;

  public Config() {
    serverConfigs = new HashMap<>();
  }

  public static Config load() {
    File file = new File(FILENAME);
    if (file.exists()) {
      var mapper = new ObjectMapper();
      try {
        return mapper.readValue(new File("config.json"), Config.class);
      } catch (Exception e) {
        e.printStackTrace();
        return new Config();
      }
    } else {
      return new Config();
    }
  }

  private void saveConfig() {
    var mapper = new ObjectMapper();
    try {
      mapper.writeValue(new File(FILENAME), this);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void save(long serverid, ServerConfig config) {
    serverConfigs.put(serverid, config);
    saveConfig();
  }

  public ServerConfig get(long serverid) {
    return serverConfigs.get(serverid);
  }

}
