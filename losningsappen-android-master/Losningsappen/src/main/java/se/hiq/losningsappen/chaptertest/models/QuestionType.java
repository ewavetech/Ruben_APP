package se.hiq.losningsappen.chaptertest.models;

import com.google.gson.annotations.SerializedName;

import se.hiq.losningsappen.R;

/**
 * Created by petterstenberg on 2014-08-26.
 * <p/>
 * Enum that keeps the state of what sort a question is and the corresponding behaviour.
 */

public enum QuestionType {

    @SerializedName("0")
    QUESTIONTYPE_TRUEORFALSE(R.layout.chapter_question_true_false) {
        @Override
        public boolean isCorrectAnswer(Question question) {
            if (question.getChoice(0).isChecked() && question.getChoice(0).isCorrectAnswer())
                return true;
            else if (question.getChoice(1).isChecked() && question.getChoice(1).isCorrectAnswer())
                return true;
            return false;
        }
    },

    @SerializedName("1")
    QUESTIONTYPE_1X2(R.layout.chapter_question_1_x_2) {
        @Override
        public boolean isCorrectAnswer(Question question) {
            if (question.getChoice(0).isChecked() && question.getChoice(0).isCorrectAnswer())
                return true;
            if (question.getChoice(1).isChecked() && question.getChoice(1).isCorrectAnswer())
                return true;
            return question.getChoice(2).isChecked() && question.getChoice(2).isCorrectAnswer();
        }
    },

    @SerializedName("2")
    QUESTIONTYPE_MULTIPLE_CHOICE(R.layout.chapter_question_multiple_choice) {
        @Override
        public boolean isCorrectAnswer(Question question) {
            if (question.getChoice(0).isChecked() != question.getChoice(0).isCorrectAnswer())
                return false;
            if (question.getChoice(1).isChecked() != question.getChoice(1).isCorrectAnswer())
                return false;
            if (question.getChoice(2).isChecked() != question.getChoice(2).isCorrectAnswer())
                return false;
            if (question.getChoice(3).isChecked() != question.getChoice(3).isCorrectAnswer())
                return false;
            if (question.getChoice(4).isChecked() != question.getChoice(4).isCorrectAnswer())
                return false;
            return true;
        }
    };

    private final int layoutResource;

    private QuestionType(int layoutResource) {
        this.layoutResource = layoutResource;
    }

    public abstract boolean isCorrectAnswer(Question question);

    public int getLayoutResource() {
        return layoutResource;
    }
}
