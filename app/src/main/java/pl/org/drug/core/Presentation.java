package pl.org.drug.core;

import java.io.Serializable;

public class Presentation implements Serializable {

  protected String title;
  protected String speaker;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSpeaker() {
    return speaker;
  }

  public void setSpeaker(String speaker) {
    this.speaker = speaker;
  }
}
