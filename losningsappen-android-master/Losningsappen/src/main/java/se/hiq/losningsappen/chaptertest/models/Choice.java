package se.hiq.losningsappen.chaptertest.models;

import java.io.Serializable;

/**
 * Created by Naknut on 04/07/14.
 */
public class Choice implements Serializable {
    private boolean isCorrectAnswer;
    private String number;
    private String text;
    private boolean isChecked = false;

    public boolean isCorrectAnswer() {
        return isCorrectAnswer;
    }

    public String getNumber() {
        return number;
    }

    public String getText() {
        return text;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
