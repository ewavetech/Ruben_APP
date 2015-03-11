package se.hiq.losningsappen.chaptertest.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Naknut on 04/07/14.
 */
public class ChapterTest implements Serializable {

    private String meta;
    private List<Question> questions;
    private String title;
    private int typeOfTest;

    public String getMeta() {
        return meta;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public String getTitle() {
        return title;
    }

    public int getTypeOfTest() {
        return typeOfTest;
    }

    public void resetAnswers() {
        for (Question question : questions) {
            for (Choice choice : question.getChoices()) {
                choice.setChecked(false);
            }
        }
    }
}
