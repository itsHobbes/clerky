package uk.co.markg.clerky.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Config {

  private static final String FILENAME = "clerky.json";

  @JsonProperty("server_config")
  private List<ServerConfig> serverConfigs;

  public Config() {
    serverConfigs = new ArrayList<>();
  }

  public static Config load() {
    File file = new File(FILENAME);
    if (file.exists()) {
      var mapper = new ObjectMapper();
      try {
        return mapper.readValue(new File(FILENAME), Config.class);
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

  public void save() {
    saveConfig();
  }

  public void addServerConfig(long serverid) {
    serverConfigs.add(
        new ServerConfig(serverid, new ArrayList<VoiceGroupConfig>(), new ArrayList<Long>()));
    saveConfig();
  }

  public void addVoiceGroup(long serverid, VoiceGroupConfig config) {
    var serverConfig = get(serverid);
    serverConfig.addVoiceGroupConfig(config);
    saveConfig();
  }

  public List<VoiceGroupConfig> getVoiceGroups(long serverid) {
    var sConfig = get(serverid);
    if (sConfig == null) {
      return Collections.emptyList();
    }
    return sConfig.getVoiceConfig();
  }

  public ServerConfig get(long serverid) {
    // TODO cleanup
    return serverConfigs.stream().filter(config -> config.getServerId() == serverid).findFirst()
        .orElse(null);
  }

  public void addStickyChannel(long serverid, long channelId) {
    var serverConfig = get(serverid);
    serverConfig.addStickyChannel(channelId);
    saveConfig();
  }

  public boolean isStickyChannel(long serverid, long channelId) {
    var serverConfig = get(serverid);
    return serverConfig.isStickyChannel(channelId);
  }

  public void removeStickyChannel(long serverid, long channelId) {
    var serverConfig = get(serverid);
    serverConfig.removeStickyChannel(channelId);
    saveConfig();
  }

}
