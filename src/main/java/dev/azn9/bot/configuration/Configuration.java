package dev.azn9.bot.configuration;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Configuration {

    //@formatter:off

    @SerializedName("discord_token") private String discordToken;
    @SerializedName("youtube_channel") private String youtubeChannel;
    @SerializedName("youtube_token") private String youtubeToken;
    @SerializedName("twitch_channel") private String twitchChannel;
    @SerializedName("twitch_token") private String twitchToken;
    @SerializedName("twitch_client_id") private String twitchClientId;
    @SerializedName("prefix") private String prefix;

    @SerializedName("guild_id") private Long guildId;
    @SerializedName("stream_announce_channel") private Long streamAnnounceChannel;
    @SerializedName("video_announce_channel") private Long videoAnnounceChannel;
    @SerializedName("settings_channel") private Long settingsChannel;
    @SerializedName("suggestions_public_channel") private Long suggestionsPublicChannel;
    @SerializedName("suggestions_admin_channel") private Long suggestionsAdminChannel;
    @SerializedName("subgoals_admin_channel") private Long subgoalsAdminChannel;
    @SerializedName("twitch_rank") private Long twitchRank;
    @SerializedName("youtube_rank") private Long youtubeRank;
    @SerializedName("pick_rank") private Long pickRank;
    @SerializedName("member_rank") private Long memberRank;

    @SerializedName("db_host") private String dbHost;
    @SerializedName("db_user") private String dbUser;
    @SerializedName("db_pass") private String dbPass;
    @SerializedName("db_name") private String dbName;

    @SerializedName("message_stream_title") private String messageStreamTitle;
    @SerializedName("message_stream_description") private String messageStreamDescription;
    @SerializedName("message_stream_color") private Integer messageStreamColor;

    @SerializedName("message_youtube") private String messageYoutube;
    @SerializedName("message_join") private String messageJoin;
    @SerializedName("message_leave") private String messageLeave;
    @SerializedName("message_settings_youtube_title") private String messageSettingsYoutubeTitle;
    @SerializedName("message_settings_youtube_description") private String messageSettingsYoutubeDescription;
    @SerializedName("message_settings_youtube_color") private Integer messageSettingsYoutubeColor;
    @SerializedName("message_settings_stream_title") private String messageSettingsStreamTitle;
    @SerializedName("message_settings_stream_description") private String messageSettingsStreamDescription;
    @SerializedName("message_settings_stream_color") private Integer messageSettingsStreamColor;

    @SerializedName("last_yt_id") private String lastYtId;

    //@formatter:on

    public void save() {
        File file = new File("config.json");
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(new Gson().toJson(this));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getDiscordToken() {
        return this.discordToken;
    }

    public String getYoutubeChannel() {
        return this.youtubeChannel;
    }

    public String getYoutubeToken() {
        return this.youtubeToken;
    }

    public String getTwitchChannel() {
        return this.twitchChannel;
    }

    public String getTwitchToken() {
        return this.twitchToken;
    }

    public Long getGuildId() {
        return this.guildId;
    }

    public Long getStreamAnnounceChannel() {
        return this.streamAnnounceChannel;
    }

    public Long getVideoAnnounceChannel() {
        return this.videoAnnounceChannel;
    }

    public Long getSettingsChannel() {
        return this.settingsChannel;
    }

    public Long getTwitchRank() {
        return this.twitchRank;
    }

    public Long getYoutubeRank() {
        return this.youtubeRank;
    }

    public Long getPickRank() {
        return this.pickRank;
    }

    public Long getMemberRank() {
        return this.memberRank;
    }

    public String getDbHost() {
        return this.dbHost;
    }

    public String getDbUser() {
        return this.dbUser;
    }

    public String getDbPass() {
        return this.dbPass;
    }

    public String getDbName() {
        return this.dbName;
    }

    public String getMessageStreamTitle() {
        return this.messageStreamTitle;
    }

    public String getMessageStreamDescription() {
        return this.messageStreamDescription;
    }

    public Integer getMessageStreamColor() {
        return this.messageStreamColor;
    }

    public String getMessageYoutube() {
        return this.messageYoutube;
    }

    public String getMessageJoin() {
        return this.messageJoin;
    }

    public String getMessageLeave() {
        return this.messageLeave;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public Long getSuggestionsAdminChannel() {
        return this.suggestionsAdminChannel;
    }

    public Long getSubgoalsAdminChannel() {
        return this.subgoalsAdminChannel;
    }

    public String getMessageSettingsYoutubeTitle() {
        return this.messageSettingsYoutubeTitle;
    }

    public String getMessageSettingsYoutubeDescription() {
        return this.messageSettingsYoutubeDescription;
    }

    public Integer getMessageSettingsYoutubeColor() {
        return this.messageSettingsYoutubeColor;
    }

    public String getMessageSettingsStreamTitle() {
        return this.messageSettingsStreamTitle;
    }

    public String getMessageSettingsStreamDescription() {
        return this.messageSettingsStreamDescription;
    }

    public Integer getMessageSettingsStreamColor() {
        return this.messageSettingsStreamColor;
    }

    public Long getSuggestionsPublicChannel() {
        return this.suggestionsPublicChannel;
    }

    public String getLastYtId() {
        return this.lastYtId;
    }

    public void setLastYtId(String videoID) {
        this.lastYtId = videoID;
        this.save();
    }

    public String getTwitchClientId() {
        return this.twitchClientId;
    }
}
