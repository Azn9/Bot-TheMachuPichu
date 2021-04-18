package dev.azn9.bot;
import dev.azn9.bot.configuration.Configuration;
import dev.azn9.bot.modules.BotModule;
import dev.azn9.bot.modules.SettingsMessageModule;
import dev.azn9.bot.modules.TwitchModule;
import dev.azn9.bot.modules.YoutubeModule;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.object.entity.channel.TextChannel;
import java.util.ArrayList;
import java.util.List;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

public class BotCore {

    private final Configuration      configuration;
    private final List<BotModule<?>> botModules = new ArrayList<>();

    public BotCore(Configuration configuration) {
        this.configuration = configuration;

        this.botModules.add(new SettingsMessageModule(configuration));
        this.botModules.add(new YoutubeModule(configuration));
        this.botModules.add(new TwitchModule(configuration));
    }

    public void start() {
        DiscordClient discordClient = DiscordClient.create(this.configuration.getDiscordToken());
        discordClient.withGateway(gatewayDiscordClient -> {
            Publisher<?> onReady = gatewayDiscordClient.on(ReadyEvent.class, readyEvent -> {
                for (BotModule<?> botModule : botModules)
                    botModule.initialize(gatewayDiscordClient);

                return gatewayDiscordClient.getGuildById(Snowflake.of(this.configuration.getGuildId())).flatMap(guild ->
                        guild.getChannelById(Snowflake.of(this.configuration.getSubgoalsAdminChannel())).flatMap(guildChannel -> {
                            if (!(guildChannel instanceof TextChannel))
                                return Mono.empty();
                            else
                                return ((TextChannel) guildChannel).getLastMessage().flatMap(message -> Mono.when(((TextChannel) guildChannel).getMessagesBefore(message.getId())));
                        }).then(guild.getChannelById(Snowflake.of(this.configuration.getSuggestionsAdminChannel())).flatMap(guildChannel -> {
                            if (!(guildChannel instanceof TextChannel))
                                return Mono.empty();
                            else
                                return ((TextChannel) guildChannel).getLastMessage().flatMap(message -> Mono.when(((TextChannel) guildChannel).getMessagesBefore(message.getId())));
                        })).then(guild.getChannelById(Snowflake.of(this.configuration.getSuggestionsPublicChannel())).flatMap(guildChannel -> {
                            if (!(guildChannel instanceof TextChannel))
                                return Mono.empty();
                            else
                                return ((TextChannel) guildChannel).getLastMessage().flatMap(message -> Mono.when(((TextChannel) guildChannel).getMessagesBefore(message.getId())));
                        })));
            });

            return Mono.when(onReady);
        }).block();
    }

}
