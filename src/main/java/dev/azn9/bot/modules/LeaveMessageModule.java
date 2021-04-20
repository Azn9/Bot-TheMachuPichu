package dev.azn9.bot.modules;

import dev.azn9.bot.configuration.Configuration;
import discord4j.core.event.domain.guild.MemberLeaveEvent;
import reactor.core.publisher.Mono;

public class LeaveMessageModule extends BotModule<MemberLeaveEvent> {

    private final Configuration configuration;

    public LeaveMessageModule(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Mono<Void> accept(MemberLeaveEvent event) {
        return Mono.empty();
    }
}
