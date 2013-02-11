package pl.org.drug.core;

import android.text.method.DateTimeKeyListener;

import java.io.Serializable;
import java.util.Date;

public class DrugEvent implements Serializable {

    protected String title;
    protected String description;

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }
}
