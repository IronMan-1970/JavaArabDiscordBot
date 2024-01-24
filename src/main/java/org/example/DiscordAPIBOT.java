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
import org.javacord.api.interaction.*;

import java.util.*;

import static org.example.LavaplayerAudioSource.play;
import static org.example.LavaplayerAudioSource.playList;
import static org.example.YouTubeSearch.youTubeSearch;

public class DiscordAPIBOT {

    public static void DiscordBot()
    {
        DiscordApi api = new DiscordApiBuilder()
                .setToken("Your_Token")
                .login().join();
        System.out.println(api.createBotInvite());

        SlashCommand.with("pray", "Allah Akbar!!!").createGlobal(api).join();
        SlashCommand.with("play", "Play any music whatever u want")
                .addOption(SlashCommandOption.createStringOption("Link","Ваша попса",true))
                        .createGlobal(api)
                        .join();
        SlashCommand.with("sieg_heil", "Хай живе перемога!!!!").createGlobal(api).join();
        SlashCommand command = SlashCommand.with("based", "classic from 80`s",
                Arrays.asList(
                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "heavy_metal", "classic Heavy Metal from 80`s"),
                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "glam", "classic Glam from 80`s"),
                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "trash_metal", "classic Trash Metal from 80`s"),
                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "hard_rock", "classic Heavy Rock from 80`s"),
                        SlashCommandOption.createWithOptions(SlashCommandOptionType.SUB_COMMAND, "rock", "classic Rock from 80`s")))
                .createGlobal(api)
                .join();


        api.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction slashCommandInteraction = event.getSlashCommandInteraction();
            TextChannel Tchannel = event.getInteraction().getChannel().get();
            String name = slashCommandInteraction.getFullCommandName().toString();
                ServerVoiceChannel channel = event.getInteraction().getUser().getConnectedVoiceChannel(event.getSlashCommandInteraction().getServer().get()).orElse(null);
                System.out.println("Server ID: " + channel.getIdAsString());
                channel.connect().thenAccept(audioConnection -> {
                    if (event.getSlashCommandInteraction().getCommandName().equals("play")){

                        playList(name,api,audioConnection,Tchannel,event.getSlashCommandInteraction().getOptionByName("link").get().getStringValue().get().toString());
                    }
                    else{
                    playList(name,api,audioConnection,Tchannel,"");}
                }).exceptionally(e -> {
                    // Failed to connect to voice channel (no permissions?)
                    e.printStackTrace();
                    return null;
                });

        });}
}
