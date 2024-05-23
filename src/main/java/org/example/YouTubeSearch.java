package org.example;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class YouTubeSearch {
    private static final String APPLICATION_NAME = "PrayDiscordBot";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String API_KEY = "AIzaSyCp1zeGiWcQpI_ikr2uBqysqC78518CUAA";

    public static List<String> youTubeSearch(String getAsk) {
        try {

            YouTube youtube = getService();
            if(Objects.equals(getAsk, "pray")){
            List<String> videoLinks = getVideoLinks(youtube, "islamic arab songs", 100);
                    return videoLinks;
                }
            else if (Objects.equals(getAsk, "militar")){
                List<String> videoLinks = getVideoLinks(youtube, "German Marches", 100);
                return videoLinks;
            }
            else if (Objects.equals(getAsk, "based heavy_metal")){
                List<String> videoLinks = getVideoLinks(youtube, "Heavy Metal from 80`s", 100);
                return videoLinks;
            }
            else if (Objects.equals(getAsk, "based glam")){
                List<String> videoLinks = getVideoLinks(youtube, "Glam rock playlist", 100);
                return videoLinks;
            }
            else if (Objects.equals(getAsk, "based trash_metal")){
                List<String> videoLinks = getVideoLinks(youtube, "Trash Metal 80`s", 100);
                return videoLinks;
            }
            else if (Objects.equals(getAsk, "based hard_rock")){
                List<String> videoLinks = getVideoLinks(youtube, "classic Heavy Rock", 100);
                return videoLinks;
            }
            else if (Objects.equals(getAsk, "based rock")){
                List<String> videoLinks = getVideoLinks(youtube, "classic Rock", 100);
                return videoLinks;
            }
            // Process the video links

        } catch (GeneralSecurityException | IOException e) {
            return Collections.singletonList("https://youtu.be/PSuJOqNls2Y");
        }
        return Collections.singletonList("https://youtu.be/PSuJOqNls2Y");
    }

    private static YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new YouTube.Builder(httpTransport, JSON_FACTORY, getHttpRequestInitializer())
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private static HttpRequestInitializer getHttpRequestInitializer() {
        return request -> request.setConnectTimeout(60000).setReadTimeout(60000);
    }

    private static List<String> getVideoLinks(YouTube youtube, String queryTerm, long maxResults) throws IOException {
        List<String> videoLinks = new ArrayList<>();

        YouTube.Search.List search = youtube.search().list("id,snippet");
        search.setKey(API_KEY);
        search.setQ(queryTerm);
        search.setType("video");
        search.setMaxResults(maxResults);

        SearchListResponse searchResponse = search.execute();
        List<SearchResult> searchResults = searchResponse.getItems();

        for (SearchResult result : searchResults) {
            String videoId = result.getId().getVideoId();
            String videoLink = "https://www.youtube.com/watch?v=" + videoId;
            videoLinks.add(videoLink);
        }

        return videoLinks;
    }

}
