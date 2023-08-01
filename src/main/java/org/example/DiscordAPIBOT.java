package org.example;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.example.LavaplayerAudioSource.playList;
import static org.example.YouTubeSearch.youTubeSearch;

public class DiscordAPIBOT {

    public static void DiscordBot()
    {
        DiscordApi api = new DiscordApiBuilder()
                .setToken("MTEzNDQ5NTczNDEwOTA1NzE5NA.G6l_Yl.wvJXy8_ovutBJjmQC4tXoYH-oEAyOk40Sx9MXA")
                .login().join();
        System.out.println(api.createBotInvite());

        SlashCommand.with("pray", "Allah Akbar!!!").createGlobal(api).join();
        SlashCommand.with("sieg_heil", "Хай живе перемога!!!!").createGlobal(api).join();
        SlashCommand.with("based", "Heavy Metal from 80`s").createGlobal(api).join();


        api.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction slashCommandInteraction = event.getSlashCommandInteraction();
            TextChannel Tchannel = event.getInteraction().getChannel().get();
            String name = slashCommandInteraction.getCommandName().toString();
                ServerVoiceChannel channel = event.getInteraction().getUser().getConnectedVoiceChannel(event.getSlashCommandInteraction().getServer().get()).orElse(null);
                System.out.println("Server ID: " + channel.getIdAsString());
                channel.connect().thenAccept(audioConnection -> {
                    playList(name,api,audioConnection,Tchannel);
                }).exceptionally(e -> {
                    // Failed to connect to voice channel (no permissions?)
                    e.printStackTrace();
                    return null;
                });

        });}
}
