package uk.co.markg.clerky.listener;

import java.awt.Color;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MentionListener extends ListenerAdapter {

  @Override
  public void onMessageReceived(MessageReceivedEvent event) {
    var members = event.getMessage().getMentionedMembers();
    var self = event.getJDA().getSelfUser();
    var ids = members.stream().map(m -> m.getIdLong()).collect(Collectors.toList());
    if (!ids.contains(self.getIdLong())) {
      return;
    }

    var builder = new EmbedBuilder();
    builder.setTitle("Clerky");
    builder.setDescription("I manage voice channels.");
    builder.addField("Source", "https://github.com/itsHobbes/clerky", false);
    builder.addField("Public?", "Yes! See GitHub", false);
    builder.setThumbnail(self.getAvatarUrl());
    builder.setColor(Color.decode("#eb7701"));
    var embed = builder.build();
    event.getChannel().sendMessage(embed).queue();
  }

}
