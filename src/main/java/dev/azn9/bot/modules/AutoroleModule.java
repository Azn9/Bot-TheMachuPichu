package dev.azn9.bot.modules;

import dev.azn9.bot.configuration.Configuration;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.guild.MemberJoinEvent;
import reactor.core.publisher.Mono;

public class AutoroleModule extends BotModule<MemberJoinEvent> {

    private final Configuration configuration;

    public AutoroleModule(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Mono<Void> accept(MemberJoinEvent event) {
        return event.getMember().addRole(Snowflake.of(this.configuration.getMemberRank()));
    }
}
