package dev.azn9.bot.modules;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.VoiceStateUpdateEvent;
import discord4j.core.object.PermissionOverwrite;
import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import reactor.core.publisher.Mono;

public class AutovocalCreateModule extends BotModule<VoiceStateUpdateEvent> {

    private static final List<Snowflake> TEMP_CHANNELS = new ArrayList<>();

    @Override
    public Mono<Void> accept(VoiceStateUpdateEvent event) {
        return event.getCurrent().getMember().flatMap(member -> {
            event.getOld().ifPresent(voiceState -> voiceState.getChannelId().ifPresent(snowflake -> {
                if (TEMP_CHANNELS.contains(snowflake))
                    voiceState.getChannel().subscribe(voiceChannel -> voiceChannel.getVoiceConnection().flux().count().subscribe(aLong -> {
                        if (aLong == 0) {
                            voiceChannel.delete().subscribe();
                            TEMP_CHANNELS.remove(snowflake);
                        }
                    }));
            }));

            return event.getCurrent().getChannel().flatMap(voiceChannel -> {
                if (voiceChannel == null)
                    return Mono.empty();

                return voiceChannel.getGuild().flatMap(guild -> guild.createVoiceChannel(voiceChannelCreateSpec -> {
                    voiceChannelCreateSpec.setName("Salon de " + member.getDisplayName());
                    voiceChannel.getCategoryId().ifPresent(voiceChannelCreateSpec::setParentId);
                    voiceChannelCreateSpec.setPermissionOverwrites(Collections.singleton(PermissionOverwrite.forMember(member.getId(), PermissionSet.of(Permission.MANAGE_CHANNELS), PermissionSet.none())));
                }).flatMap(voiceChannel1 -> {
                    TEMP_CHANNELS.add(voiceChannel1.getId());

                    return member.edit(guildMemberEditSpec -> guildMemberEditSpec.setNewVoiceChannel(voiceChannel1.getId()));
                }));
            });
        });
    }
}
