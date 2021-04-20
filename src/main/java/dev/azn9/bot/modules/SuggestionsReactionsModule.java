package dev.azn9.bot.modules;

import dev.azn9.bot.configuration.Configuration;
import discord4j.core.event.domain.message.ReactionAddEvent;
import reactor.core.publisher.Mono;

public class SuggestionsReactionsModule extends BotModule<ReactionAddEvent> {

    private final Configuration configuration;

    public SuggestionsReactionsModule(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Mono<Void> accept(ReactionAddEvent event) {
        return Mono.empty();
    }
}
