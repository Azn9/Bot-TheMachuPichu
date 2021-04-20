package dev.azn9.bot.modules;

import dev.azn9.bot.configuration.Configuration;
import discord4j.core.event.domain.guild.MemberJoinEvent;
import reactor.core.publisher.Mono;

public class JoinMessageModule extends BotModule<MemberJoinEvent> {

    private final Configuration configuration;

    public JoinMessageModule(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Mono<Void> accept(MemberJoinEvent event) {
        return Mono.empty();
    }
}
