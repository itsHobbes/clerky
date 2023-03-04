package uk.co.markg.clerky.command;

import java.awt.Color;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

@CommandInfo(name = "giveawaystart", description = "Starts a giveaway event")
public class GiveawayStart implements Command {

  private static final String CHANNEL_ARG = "targetchannel";
  private static final String TITLE_ARG = "title";
  private static final String MESSAGE_ARG = "message";
  private static final String WINNERS_ARG = "numofwinners";
  private static final String DATE_ARG = "enddate";
  private static final String ROLE_ARG = "rolerequirement";
  private static final String CONFIRM_ARG = "confirm";

  private static final String EVENT_INSTRUCTIONS =
      "\n\nTo enter, click the **'interested'** button on the event!";

  @Override
  public List<Permission> getPermissions() {
    return List.of(Permission.BAN_MEMBERS);
  }

  @Override
  public List<OptionData> defineOptions() {
    List<OptionData> options = new ArrayList<>();
    options.add(new OptionData(OptionType.CHANNEL, CHANNEL_ARG,
        "Target announcement channel to post the giveaway", true));
    options.add(new OptionData(OptionType.STRING, TITLE_ARG, "Title for the giveaway", true));
    options.add(new OptionData(OptionType.STRING, MESSAGE_ARG, "The giveaway message", true));
    options.add(new OptionData(OptionType.INTEGER, WINNERS_ARG,
        "Number of winners to draw from the giveaway", true));
    options.add(
        new OptionData(OptionType.STRING, DATE_ARG, "Unix time for ending the giveaway", true));
    options.add(new OptionData(OptionType.ROLE, ROLE_ARG,
        "Required role for participation in the giveaway", false));
    options.add(
        new OptionData(OptionType.BOOLEAN, CONFIRM_ARG, "Confirm the details are correct", false));
    return options;
  }

  @Override
  public void execute(SlashCommandInteractionEvent event) {
    var targetChannel = event.getOption(CHANNEL_ARG, OptionMapping::getAsChannel);

    event.deferReply().queue();

    // if (!targetChannel.getType().equals(ChannelType.NEWS)) {
    // event.getHook().editOriginal("Target channel must be an announcement channel.").queue();
    // return;
    // }

    var channel = targetChannel.asTextChannel();
    if (!channel.canTalk()) {
      event.getHook()
          .editOriginal("I don't have permission to send in " + channel.getAsMention() + ".")
          .queue();
      return;
    }

    if (!event.getGuild().getSelfMember().hasPermission(Permission.MANAGE_EVENTS)) {
      event.getHook().editOriginal("I don't have permission to create events in this server.")
          .queue();
      return;
    }

    var endDate = event.getOption(DATE_ARG, OptionMapping::getAsString);
    LocalDateTime epochSecond;
    try {
      epochSecond = LocalDateTime.ofEpochSecond(Long.parseLong(endDate), 0, ZoneOffset.UTC);
    } catch (DateTimeException e) {
      event.getHook().editOriginal("I can't parse that end date. Try again with a unix timestamp.")
          .queue();
      return;
    }

    var endDateTime = OffsetDateTime.of(epochSecond, ZoneOffset.UTC);

    if (endDateTime.isBefore(OffsetDateTime.now())) {
      event.getHook().editOriginal("End date cannot be in the past. I'm not Doctor Who.").queue();
      return;
    }

    startGiveaway(event, channel, endDateTime);
  }

  private void startGiveaway(SlashCommandInteractionEvent event, TextChannel channel,
      OffsetDateTime endDateTime) {

    var title = event.getOption(TITLE_ARG, OptionMapping::getAsString);
    var message = event.getOption(MESSAGE_ARG, OptionMapping::getAsString);
    var numOfWinners = event.getOption(WINNERS_ARG, OptionMapping::getAsInt);
    var role = event.getOption(ROLE_ARG, OptionMapping::getAsRole);
    var confirm = event.getOption(CONFIRM_ARG, OptionMapping::getAsBoolean);

    var embed = new EmbedBuilder().setAuthor(event.getMember().getEffectiveName());
    embed.setTitle("Giveaway: " + TITLE_ARG).setDescription(MESSAGE_ARG + EVENT_INSTRUCTIONS);
    embed.setColor(Color.decode("#eb7701"));
    embed.addField("Start time", "Now", true);

    var endDate = event.getOption(DATE_ARG, OptionMapping::getAsString);
    embed.addField("End time", "<t:" + endDate + ":f>", true);
    embed.addField("", "", true);
    embed.addField("Number of winners:", String.valueOf(numOfWinners), false);
    embed.setThumbnail(event.getMember().getAvatarUrl());

    if (confirm == null) {
      confirm = Boolean.FALSE;
    }

    if (!confirm) {
      if (role != null) {
        embed.addField("Role requirement", event.getGuild().getRoleById(role.getId()).getName(),
            false);
      }
      event.getHook()
          .editOriginal("If this preview is correct, rerun the command with the confirm option")
          .queue();
      event.getChannel().sendMessageEmbeds(embed.build()).queue();
      return;
    }
    var startDateTime = endDateTime.minusMinutes(5);

    var eventBuilder = event.getGuild().createScheduledEvent("Giveaway: " + title,
        event.getGuild().getName(), startDateTime, endDateTime);
    var roleText =
        role == null ? "" : "\n\n**You must have the " + role.getAsMention() + " role to enter!**";
    eventBuilder.setDescription(message + roleText + EVENT_INSTRUCTIONS);
    var eventFuture = eventBuilder.submit();

    eventFuture.thenAccept(e -> {
      var m = role == null ? "There is a new giveaway!"
          : "There is a new giveaway for " + role.getAsMention() + " members!";
      event.getGuild().getTextChannelById(channel.getId())
          .sendMessage(m + "\nhttps://discord.com/events/" + e.getGuild().getId() + "/" + e.getId())
          .queue();
    });

    event.getHook().editOriginal("Event created!").queue();
  }

}
