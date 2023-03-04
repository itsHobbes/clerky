package uk.co.markg.clerky.listener;

import net.dv8tion.jda.api.events.guild.scheduledevent.update.ScheduledEventUpdateStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ScheduledEvent extends ListenerAdapter {

  @Override
  public void onScheduledEventUpdateStatus(ScheduledEventUpdateStatusEvent event) {
    switch (event.getNewStatus()) {
      case ACTIVE:
        break;
      case CANCELED:
        break;
      default:
        break;
    }
  }
}
