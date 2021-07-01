package com.google;

import java.util.ArrayList;
import java.util.List;

/** A class used to represent a Playlist */
class VideoPlaylist {

  private final String title;
  private List<Video> videos = new ArrayList<>();

  VideoPlaylist(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public List<Video> getVideos() {
    return videos;
  }

  public void addVideo(Video video) {
    videos.add(video);
  }

  public void removeVideo(Video video) {
    videos.remove(video);
  }

  public void clearVideos() {
    videos = new ArrayList<>();
  }

  public String videosString() {
    if (videos.isEmpty()) {
      return "No videos here yet";
    }

    StringBuilder videosStr = new StringBuilder();

    for (Video video : videos) {
      videosStr.append(video).append("\n");
    }

    return videosStr.deleteCharAt(videosStr.length() - 1).toString();
  }
}
