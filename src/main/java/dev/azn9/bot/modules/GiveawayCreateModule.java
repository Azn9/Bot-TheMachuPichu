package dev.azn9.bot.modules;

import dev.azn9.bot.configuration.Configuration;
import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

public class GiveawayCreateModule extends BotModule<MessageCreateEvent> {

    private final Configuration configuration;

    public GiveawayCreateModule(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Mono<Void> accept(MessageCreateEvent event) {
        return Mono.empty();
    }

}
