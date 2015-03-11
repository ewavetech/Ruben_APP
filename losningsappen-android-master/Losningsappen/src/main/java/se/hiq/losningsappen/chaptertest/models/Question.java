package se.hiq.losningsappen.chaptertest.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Naknut on 04/07/14.
 */

public class Question implements Serializable {
    private List<Choice> choices;
    private int number;
    private String text;
    private QuestionType typeOfQuestion;
    private String imgPath;

    public List<Choice> getChoices() {
        return choices;
    }

    public int getNumber() {
        return number;
    }

    public String getText() {
        return text;
    }

    public QuestionType getTypeOfQuestion() {
        return typeOfQuestion;
    }

    public Choice getChoice(int position) {
        return choices.get(position);
    }

    public String getImgPath() {
        return imgPath;
    }

    public void clearAnswers() {
        for (Choice choice : choices) {
            choice.setChecked(false);
        }
    }
}
