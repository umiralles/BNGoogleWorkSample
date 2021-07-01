package com.google;

import java.util.Collections;
import java.util.List;

/** A class used to represent a video. */
class Video implements Comparable<Video> {

  private final String title;
  private final String videoId;
  private final List<String> tags;

  private String flaggedReason = null;

  Video(String title, String videoId, List<String> tags) {
    this.title = title;
    this.videoId = videoId;
    this.tags = Collections.unmodifiableList(tags);
  }

  /** Returns the title of the video. */
  String getTitle() {
    return title;
  }

  /** Returns the video id of the video. */
  String getVideoId() {
    return videoId;
  }

  /** Returns a readonly collection of the tags of the video. */
  List<String> getTags() {
    return tags;
  }

  boolean isFlagged() {
    return flaggedReason != null;
  }

  String getFlaggedReason() {
    return flaggedReason;
  }

  void setFlaggedReason(String reason) {
    this.flaggedReason = reason;
  }

  @Override
  public int compareTo(Video video) {
    return title.compareTo(video.title);
  }

  @Override
  public String toString() {
    StringBuilder videoStr = new StringBuilder();

    videoStr.append(title);
    videoStr.append(" (").append(videoId).append(")");
    videoStr.append(" [");

    if (!tags.isEmpty()) {
      for (String tag : tags) {
        videoStr.append(tag).append(" ");
      }

      videoStr.deleteCharAt(videoStr.length() - 1);
    }

    videoStr.append("]");

    if (isFlagged()) {
      videoStr.append(" - FLAGGED (reason: ").append(getFlaggedReason()).append(")");
    }

    return videoStr.toString();
  }
}
