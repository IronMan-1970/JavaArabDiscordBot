package org.example;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import org.javacord.api.DiscordApi;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.audio.AudioSourceBase;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;

import java.util.List;

import static org.example.YouTubeSearch.youTubeSearch;

public class LavaplayerAudioSource extends AudioSourceBase {


    private final AudioPlayer audioPlayer;
    private AudioFrame lastFrame;

    /**
     * Creates a new lavaplayer audio source.
     *
     * @param api A discord api instance.
     * @param audioPlayer An audio player from Lavaplayer.
     */
    public LavaplayerAudioSource(DiscordApi api, AudioPlayer audioPlayer) {
        super(api);
        this.audioPlayer = audioPlayer;
    }

    @Override
    public byte[] getNextFrame() {
        if (lastFrame == null) {
            return null;
        }
        return applyTransformers(lastFrame.getData());
    }

    @Override
    public boolean hasFinished() {
        return false;
    }

    @Override
    public boolean hasNextFrame() {
        lastFrame = audioPlayer.provide();
        return lastFrame != null;
    }

    @Override
    public AudioSource copy() {
        return new LavaplayerAudioSource(getApi(), audioPlayer);
    }

    public static void playList(String getAsk, DiscordApi api, AudioConnection audioConnection, TextChannel channel ) {

        List<String> list = youTubeSearch(getAsk);
        int i = 0;

        Message initialMessage = new MessageBuilder()
                .setContent("Click on one of these Buttons!")
                .addComponents(
                        ActionRow.of(
                                Button.success("previous", "previous"),
                                Button.success("next", "next")))
                .send(channel).join();

        api.addServerVoiceChannelMemberLeaveListener(event -> {
            // Replace "YOUR_BOT_ID" with the ID of your bot user
            if (event.getUser().isBot()) {
                initialMessage.delete();
            }
        });
        play(list, api, audioConnection, i, channel,initialMessage);
    }

    public static void play( List<String> list,DiscordApi api, AudioConnection audioConnection,int i ,TextChannel channel, Message initialMessage){
        AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        playerManager.registerSourceManager(new YoutubeAudioSourceManager());
        AudioPlayer player = playerManager.createPlayer();

            AudioSource source = new LavaplayerAudioSource(api, player);
            audioConnection.setAudioSource(source);
            playerManager.loadItem(list.get(i), new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack track) {
                    String trackName = track.getInfo().title;
                    initialMessage.edit(trackName);
                    player.playTrack(track);


                    long durationInMillis = track.getDuration();
                    try {
                        Thread.sleep(durationInMillis);
                        play(list, api,  audioConnection, i+1 , channel,initialMessage);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }

                @Override
                public void playlistLoaded(AudioPlaylist playlist) {
                    for (AudioTrack track : playlist.getTracks()) {
                        player.playTrack(track);
                    }
                }

                @Override
                public void noMatches() {
                    // Notify the user that we've got nothing
                }

                @Override
                public void loadFailed(FriendlyException throwable) {
                    // Notify the user that everything exploded
                }
            });



        api.addButtonClickListener(buttonClickEvent -> {
            if(buttonClickEvent.getButtonInteraction().getCustomId().equals("previous"))
            {play( list, api,  audioConnection, i-1 , channel,initialMessage);}
            else if (buttonClickEvent.getButtonInteraction().getCustomId().equals("next"))
            { play(list, api,  audioConnection, i+1 , channel,initialMessage);}
        });

    }
}