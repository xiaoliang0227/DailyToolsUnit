package com.zyl.tools.dailytoolsunit.model;

import com.zyl.tools.dailytoolsunit.enumeration.MediaType;

import java.io.Serializable;

/**
 * Created by JasonZhao on 15/11/4.
 */
public class MediaModel implements Serializable {

  private int id;

  private String thumbPath;

  private String path;

  private String title;

  private String displayName;

  private String mimeType;

  private MediaType type;

  private long size;

  private long last_modified;

  private int height;

  private int width;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getThumbPath() {
    return thumbPath;
  }

  public void setThumbPath(String thumbPath) {
    this.thumbPath = thumbPath;
  }

  public String getMimeType() {
    return mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  public MediaType getType() {
    return type;
  }

  public void setType(MediaType type) {
    this.type = type;
  }

  public long getSize() {
    return size / 1024;
  }

  public void setSize(long size) {
    this.size = size;
  }

  public long getLast_modified() {
    return last_modified;
  }

  public void setLast_modified(long last_modified) {
    this.last_modified = last_modified;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  @Override
  public String toString() {
    return "MediaModel{" +
        "id=" + id +
        ", thumbPath='" + thumbPath + '\'' +
        ", path='" + path + '\'' +
        ", title='" + title + '\'' +
        ", displayName='" + displayName + '\'' +
        ", mimeType='" + mimeType + '\'' +
        ", type=" + type +
        ", size=" + size +
        ", last_modified=" + last_modified +
        ", height=" + height +
        ", width=" + width +
        '}';
  }
}
