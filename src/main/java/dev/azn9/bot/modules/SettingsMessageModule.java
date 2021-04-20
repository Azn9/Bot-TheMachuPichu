package dev.azn9.bot.modules;

import dev.azn9.bot.configuration.Configuration;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.ReactionAddEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.object.reaction.ReactionEmoji;
import discord4j.rest.util.Color;
import java.util.ArrayList;
import java.util.List;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

public class SettingsMessageModule extends BotModule<ReactionAddEvent> {

    private final Configuration                      botConfiguration;
    private final SettingsMessageModuleConfiguration moduleConfiguration;

    public SettingsMessageModule(Configuration botConfiguration) {
        this.botConfiguration = botConfiguration;
        this.moduleConfiguration = new SettingsMessageModuleConfiguration();
        this.moduleConfiguration.channelId = botConfiguration.getSettingsChannel();
        this.moduleConfiguration.messageTitle.add(botConfiguration.getMessageSettingsYoutubeTitle());
        this.moduleConfiguration.messageTitle.add(botConfiguration.getMessageSettingsStreamTitle());
        this.moduleConfiguration.messageDescription.add(botConfiguration.getMessageSettingsYoutubeDescription());
        this.moduleConfiguration.messageDescription.add(botConfiguration.getMessageSettingsStreamDescription());
        this.moduleConfiguration.messageColors.add(botConfiguration.getMessageSettingsYoutubeColor());
        this.moduleConfiguration.messageColors.add(botConfiguration.getMessageSettingsStreamColor());
        this.moduleConfiguration.roleId.add(botConfiguration.getYoutubeRank());
        this.moduleConfiguration.roleId.add(botConfiguration.getTwitchRank());
    }

    @Override
    public void initialize(GatewayDiscordClient gatewayDiscordClient) {
        gatewayDiscordClient.getGuildById(Snowflake.of(this.botConfiguration.getGuildId())).subscribe(guild ->
                guild.getChannelById(Snowflake.of(this.moduleConfiguration.channelId)).subscribe(guildChannel -> {
                    if (!(guildChannel instanceof TextChannel))
                        return;

                    ((TextChannel) guildChannel).getLastMessage().flatMap(message -> Mono.when(((TextChannel) guildChannel).getMessagesBefore(message.getId()).flatMap(Message::delete)).flatMap(unused -> ((TextChannel) guildChannel).createMessage(messageCreateSpec -> {
                        messageCreateSpec.setEmbed(embedCreateSpec -> {
                            embedCreateSpec.setTitle(this.moduleConfiguration.messageTitle.get(0));
                            embedCreateSpec.setTitle(this.moduleConfiguration.messageDescription.get(0));
                            embedCreateSpec.setColor(Color.of(this.moduleConfiguration.messageColors.get(0)));
                        });
                    }).flatMap(message1 -> {
                        this.moduleConfiguration.messageIds.add(message1.getId().asLong());
                        return message1.addReaction(ReactionEmoji.unicode(this.moduleConfiguration.reactionsRaw.getT1())).then(message1.addReaction(ReactionEmoji.unicode(this.moduleConfiguration.reactionsRaw.getT2()))).then(((TextChannel) guildChannel).createMessage(messageCreateSpec -> {
                            messageCreateSpec.setEmbed(embedCreateSpec -> {
                                embedCreateSpec.setTitle(this.moduleConfiguration.messageTitle.get(1));
                                embedCreateSpec.setTitle(this.moduleConfiguration.messageDescription.get(1));
                                embedCreateSpec.setColor(Color.of(this.moduleConfiguration.messageColors.get(1)));
                            });
                        }).flatMap(message2 -> {
                            this.moduleConfiguration.messageIds.add(message2.getId().asLong());
                            return message2.addReaction(ReactionEmoji.unicode(this.moduleConfiguration.reactionsRaw.getT1())).then(message2.addReaction(ReactionEmoji.unicode(this.moduleConfiguration.reactionsRaw.getT2())));
                        }));
                    }))).subscribe();
                }));
    }

    @Override
    public Mono<Void> accept(ReactionAddEvent event) {
        if (!event.getMember().isPresent())
            return Mono.empty();

        if (event.getMember().get().isBot())
            return Mono.empty();

        return event.getMessage().flatMap(message -> {
            if (!event.getEmoji().asUnicodeEmoji().isPresent())
                return Mono.empty();

            if (this.moduleConfiguration.messageIds.contains(message.getId().asLong()))
                return Mono.empty();

            int index = this.moduleConfiguration.messageIds.indexOf(message.getId().asLong());

            String raw = event.getEmoji().asUnicodeEmoji().get().getRaw();

            if (raw.equalsIgnoreCase(this.moduleConfiguration.reactionsRaw.getT1()))
                return event.getMember().get().addRole(Snowflake.of(this.moduleConfiguration.roleId.get(index))).then(message.removeReaction(event.getEmoji(), event.getUserId()));
            else if (raw.equalsIgnoreCase(this.moduleConfiguration.reactionsRaw.getT2()))
                return event.getMember().get().removeRole(Snowflake.of(this.moduleConfiguration.roleId.get(index))).then(message.removeReaction(event.getEmoji(), event.getUserId()));
            else
                return message.removeReaction(event.getEmoji(), event.getUserId());
        });
    }

    public static class SettingsMessageModuleConfiguration {

        public Long                   channelId;
        public List<Long>             messageIds         = new ArrayList<>();
        public List<Long>             roleId             = new ArrayList<>();
        public Tuple2<String, String> reactionsRaw       = Tuples.of("✅", "🚫");
        public List<String>           messageTitle       = new ArrayList<>();
        public List<String>           messageDescription = new ArrayList<>();
        public List<Integer>          messageColors      = new ArrayList<>();

    }

}
