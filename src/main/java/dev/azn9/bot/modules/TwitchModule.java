package dev.azn9.bot.modules;

import JSon.JSONArray;
import JSon.JSONObject;
import dev.azn9.bot.configuration.Configuration;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import discord4j.rest.util.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import reactor.core.publisher.Mono;

public class TwitchModule extends BotModule<ReadyEvent> {

    private final Configuration botConfiguration;

    public TwitchModule(Configuration botConfiguration) {
        this.botConfiguration = botConfiguration;
    }

    @Override
    public void initialize(GatewayDiscordClient gatewayDiscordClient) {
        gatewayDiscordClient.getGuildById(Snowflake.of(this.botConfiguration.getGuildId())).subscribe(guild -> guild.getChannelById(Snowflake.of(this.botConfiguration.getStreamAnnounceChannel())).subscribe(guildChannel -> {
            if (!(guildChannel instanceof GuildMessageChannel))
                return;

            new Timer().scheduleAtFixedRate(new TimerTask() {

                boolean streaming = false;

                @Override
                public void run() {
                    try {
                        URL url = new URL("https://api.twitch.tv/helix/streams/?user_id=" + botConfiguration.getTwitchChannel());
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setRequestMethod("GET");
                        con.setRequestProperty("Authorization", "Bearer " + botConfiguration.getTwitchToken());
                        con.setRequestProperty("Client-Id", botConfiguration.getTwitchClientId());

                        int status = con.getResponseCode();

                        if (status == 200) {
                            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(con.getInputStream()));
                            String inputLine;
                            StringBuilder content = new StringBuilder();
                            while ((inputLine = in.readLine()) != null)
                                content.append(inputLine);
                            in.close();

                            JSONObject jsonObject = new JSONObject(content.toString());
                            JSONArray data = jsonObject.getJSONArray("data");

                            if (!data.isEmpty()) {
                                if (streaming)
                                    return;

                                streaming = true;

                                JSONObject streamData = data.getJSONObject(0);

                                if (streamData.getString("type").equalsIgnoreCase("live"))
                                    ((GuildMessageChannel) guildChannel).createEmbed(embedCreateSpec -> {
                                        embedCreateSpec.setTitle(botConfiguration.getMessageStreamTitle());
                                        embedCreateSpec.setDescription(botConfiguration.getMessageStreamDescription());
                                        embedCreateSpec.setColor(Color.of(botConfiguration.getMessageStreamColor()));
                                        embedCreateSpec.addField("Titre", streamData.getString("title"), true);
                                        embedCreateSpec.addField("Jeu", streamData.getString("game_name"), true);
                                    }).subscribe();
                            } else
                                streaming = false;
                        } else
                            throw new IOException(status + " : " + con.getResponseMessage());

                        con.disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, 0L, 30000L);
        }));
    }

    @Override
    public Mono<Void> accept(ReadyEvent event) {
        return Mono.empty();
    }

}
