package dev.azn9.bot.modules;

import JSon.JSONArray;
import JSon.JSONObject;
import dev.azn9.bot.configuration.Configuration;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import reactor.core.publisher.Mono;

public class YoutubeModule extends BotModule<ReadyEvent> {

    private final Configuration botConfiguration;

    public YoutubeModule(Configuration botConfiguration) {
        this.botConfiguration = botConfiguration;
    }

    @Override
    public void initialize(GatewayDiscordClient gatewayDiscordClient) {
        gatewayDiscordClient.getGuildById(Snowflake.of(this.botConfiguration.getGuildId())).subscribe(guild -> guild.getChannelById(Snowflake.of(this.botConfiguration.getVideoAnnounceChannel())).subscribe(guildChannel -> {
            if (!(guildChannel instanceof GuildMessageChannel))
                return;

            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    try {
                        String inputLine;
                        String apiKey = botConfiguration.getYoutubeToken();
                        String channel = botConfiguration.getYoutubeChannel();
                        URL url = new URL("https://www.googleapis.com/youtube/v3/search?channelId=" + channel + "&order=date&part=snippet&type=video&maxResults=1&key=" + apiKey);
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setRequestMethod("GET");
                        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        StringBuilder line = new StringBuilder();
                        while ((inputLine = in.readLine()) != null)
                            line.append(inputLine);
                        in.close();

                        JSONObject jsonObject = new JSONObject(line.toString());
                        if (!jsonObject.isNull("items")) {
                            JSONArray items = jsonObject.getJSONArray("items");
                            if (!items.isNull(0)) {
                                JSONObject item = items.getJSONObject(0);
                                if (!item.isNull("id")) {
                                    String videoID = item.getJSONObject("id").getString("videoId");

                                    if (botConfiguration.getLastYtId().equalsIgnoreCase(videoID))
                                        return;

                                    botConfiguration.setLastYtId(videoID);

                                    ((GuildMessageChannel) guildChannel).createMessage(messageCreateSpec -> messageCreateSpec.setContent(String.format(botConfiguration.getMessageYoutube(), videoID))).subscribe();
                                }
                            }
                        }
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
