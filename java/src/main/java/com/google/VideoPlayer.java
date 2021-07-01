package com.google;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class VideoPlayer {

  private final VideoLibrary videoLibrary;
  private Video nowPlaying = null;
  private boolean pausedVideo = false;

  private final Map<String, VideoPlaylist> playlists = new HashMap<>();

  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  public void showAllVideos() {
    StringBuilder allVideos = new StringBuilder();
    allVideos.append("Here's a list of all available videos:\n");

    // Append each video to the list in order
    List<Video> videos = videoLibrary.getVideos();
    Collections.sort(videos);

    for (Video video : videos) {
      allVideos.append(video.toString()).append("\n");
    }

    System.out.print(allVideos);
  }

  public void playVideo(String videoId) {
    Video toPlay = videoLibrary.getVideo(videoId);

    if (toPlay == null) {
      System.out.println("Cannot play video: Video does not exist");
      return;
    }

    if (toPlay.isFlagged()) {
      System.out.print("Cannot play video: Video is currently flagged ");
      System.out.println("(reason: " + toPlay.getFlaggedReason() + ")");
      return;
    }

    StringBuilder playVideo = new StringBuilder();

    if (nowPlaying != null) {
      playVideo.append("Stopping video: ");
      playVideo.append(nowPlaying.getTitle()).append("\n");
    }

    playVideo.append("Playing video: ");
    playVideo.append(toPlay.getTitle());
    nowPlaying = toPlay;
    pausedVideo = false;

    System.out.println(playVideo);
  }

  public void stopVideo() {
    StringBuilder stopVideo = new StringBuilder();

    if (nowPlaying != null) {
      stopVideo.append("Stopping video: ");
      stopVideo.append(nowPlaying.getTitle());
      nowPlaying = null;
    } else {
      stopVideo.append("Cannot stop video: No video is currently playing");
    }

    System.out.println(stopVideo);
  }

  public void playRandomVideo() {
    List<Video> videos = videoLibrary.getNonFlaggedVideos();

    if (videos.isEmpty()) {
      System.out.println("No videos available");
      return;
    }
    Random rand = new Random();
    int index = rand.nextInt() % videos.size();

    if (index < 0) {
      index += videos.size();
    }

    playVideo(videos.get(index).getVideoId());
  }

  public void pauseVideo() {
    if (nowPlaying == null) {
      System.out.println("Cannot pause video: No video is currently playing");
      return;
    }

    if (pausedVideo) {
      System.out.println("Video already paused: " + nowPlaying.getTitle());
      return;
    }

    System.out.println("Pausing video: " + nowPlaying.getTitle());
    pausedVideo = true;
  }

  public void continueVideo() {
    if (nowPlaying == null) {
      System.out.println("Cannot continue video: No video is currently playing");
      return;
    }

    if (!pausedVideo) {
      System.out.println("Cannot continue video: Video is not paused");
      return;
    }

    System.out.println("Continuing video: " + nowPlaying.getTitle());
    pausedVideo = false;
  }

  public void showPlaying() {
    if (nowPlaying == null) {
      System.out.println("No video is currently playing");
      return;
    }

    String paused = pausedVideo ? " - PAUSED" : "";
    System.out.println("Currently playing: " + nowPlaying + paused);
  }

  public void createPlaylist(String playlistName) {
    if (playlists.containsKey(playlistName.toLowerCase())) {
      System.out.println("Cannot create playlist: A playlist with the same name already exists");
      return;
    }

    VideoPlaylist newPlaylist = new VideoPlaylist(playlistName);
    playlists.put(playlistName.toLowerCase(), newPlaylist);
    System.out.println("Successfully created new playlist: " + newPlaylist.getTitle());
  }

  public void addVideoToPlaylist(String playlistName, String videoId) {
    if (!playlists.containsKey(playlistName.toLowerCase())) {
      System.out.println("Cannot add video to " + playlistName + ": Playlist does not exist");
      return;
    }

    Video toAdd = videoLibrary.getVideo(videoId);

    if (toAdd == null) {
      System.out.println("Cannot add video to " + playlistName + ": Video does not exist");
      return;
    }

    if (toAdd.isFlagged()) {
      System.out.print("Cannot add video to " + playlistName + ": Video is currently flagged ");
      System.out.println("(reason: " + toAdd.getFlaggedReason() + ")");
      return;
    }

    VideoPlaylist playlist = playlists.get(playlistName.toLowerCase());
    List<Video> videos = playlist.getVideos();

    if (videos.contains(toAdd)) {
      System.out.println("Cannot add video to " + playlistName + ": Video already added");
      return;
    }

    playlist.addVideo(toAdd);
    System.out.println("Added video to " + playlistName + ": " + toAdd.getTitle());
  }

  public void showAllPlaylists() {
    if (playlists.isEmpty()) {
      System.out.println("No playlists exist yet");
      return;
    }

    StringBuilder allPlaylists = new StringBuilder();
    allPlaylists.append("Showing all playlists: \n");

    for (VideoPlaylist playlist : playlists.values()) {
      allPlaylists.append(playlist.getTitle()).append("\n");
    }

    System.out.print(allPlaylists);
  }

  public void showPlaylist(String playlistName) {
    if (!playlists.containsKey(playlistName.toLowerCase())) {
      System.out.println("Cannot show playlist " + playlistName + ": Playlist does not exist");
      return;
    }

    VideoPlaylist playlist = playlists.get(playlistName.toLowerCase());
    System.out.println("Showing playlist: " + playlistName);
    System.out.println(playlist.videosString());
  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    if (!playlists.containsKey(playlistName.toLowerCase())) {
      System.out.println("Cannot remove video from " + playlistName + ": Playlist does not exist");
      return;
    }

    Video toRemove = videoLibrary.getVideo(videoId);

    if (toRemove == null) {
      System.out.println("Cannot remove video from " + playlistName + ": Video does not exist");
      return;
    }

    VideoPlaylist playlist = playlists.get(playlistName.toLowerCase());

    if (!playlist.getVideos().contains(toRemove)) {
      System.out.println("Cannot remove video from " + playlistName + ": Video is not in playlist");
      return;
    }

    playlist.removeVideo(toRemove);
    System.out.println("Removed video from " + playlistName + ": " + toRemove.getTitle());
  }

  public void clearPlaylist(String playlistName) {
    if (!playlists.containsKey(playlistName.toLowerCase())) {
      System.out.println("Cannot clear playlist " + playlistName + ": Playlist does not exist");
      return;
    }

    VideoPlaylist playlist = playlists.get(playlistName.toLowerCase());

    playlist.clearVideos();
    System.out.println("Successfully removed all videos from " + playlistName);
  }

  public void deletePlaylist(String playlistName) {
    if (!playlists.containsKey(playlistName.toLowerCase())) {
      System.out.println("Cannot delete playlist " + playlistName + ": Playlist does not exist");
      return;
    }

    playlists.remove(playlistName.toLowerCase());
    System.out.println("Deleted playlist: " + playlistName);
  }

  public void searchVideos(String searchTerm) {
    List<Video> videos = videoLibrary.getNonFlaggedVideos();
    Collections.sort(videos);
    String lowSearchTerm = searchTerm.toLowerCase();

    List<Video> results = new ArrayList<>();

    for (Video video : videos) {
      if (video.getTitle().toLowerCase().contains(lowSearchTerm)) {
        results.add(video);
      }
    }

    displayAndPlaySearchResult(searchTerm, results);
  }

  public void searchVideosWithTag(String videoTag) {
    List<Video> videos = videoLibrary.getNonFlaggedVideos();
    Collections.sort(videos);
    String lowVideoTag = videoTag.toLowerCase();

    List<Video> results = new ArrayList<>();

    for (Video video : videos) {
      if (video.getTags().contains(lowVideoTag)) {
        results.add(video);
      }
    }

    displayAndPlaySearchResult(videoTag, results);
  }

  private void displayAndPlaySearchResult(String searchTerm, List<Video> results) {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    if (results.isEmpty()) {
      System.out.println("No search results for " + searchTerm);
      return;
    }

    StringBuilder resultsStr = new StringBuilder();
    resultsStr.append("Here are the results for ");
    resultsStr.append(searchTerm).append(":\n");

    for (int i = 0; i < results.size(); i++) {
      resultsStr.append(i + 1).append(") ");
      resultsStr.append(results.get(i)).append("\n");
    }

    resultsStr.append("Would you like to play any of the above? ");
    resultsStr.append("If yes, specify the number of the video.\n");

    resultsStr.append("If your answer is not a valid number, we will assume it's a no.");

    System.out.println(resultsStr);

    int selectionIndex;
    try {
      String selection = reader.readLine();
      selectionIndex = Integer.parseInt(selection);

    } catch (Exception e) {
      return;
    }

    if (!(selectionIndex > 0 && selectionIndex <= results.size())) {
      return;
    }

    playVideo(results.get(selectionIndex - 1).getVideoId());
  }

  public void flagVideo(String videoId) {
    flagVideo(videoId, "Not supplied");
  }

  public void flagVideo(String videoId, String reason) {
    Video toFlag = videoLibrary.getVideo(videoId);

    if (toFlag == null) {
      System.out.println("Cannot flag video: Video does not exist");
      return;
    }

    if (toFlag.isFlagged()) {
      System.out.println("Cannot flag video: Video is already flagged");
      return;
    }

    if (toFlag.equals(nowPlaying)) {
      stopVideo();
    }

    toFlag.setFlaggedReason(reason);
    System.out.print("Successfully flagged video: " + toFlag.getTitle());
    System.out.println(" (reason: " + reason + ")");
  }

  public void allowVideo(String videoId) {
    System.out.println("allowVideo needs implementation");
  }
}