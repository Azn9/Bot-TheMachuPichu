package dev.azn9.bot.modules;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import java.lang.reflect.ParameterizedType;
import reactor.core.publisher.Mono;

public abstract class BotModule<T extends Event> {

    public void initialize(GatewayDiscordClient gatewayDiscordClient) {

    }

    public abstract Mono<Void> accept(T event);

    @SuppressWarnings({"unchecked", "rawtypes"})
    public Class<T> getEventClass() {
        return ((Class) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0]);
    }
}
