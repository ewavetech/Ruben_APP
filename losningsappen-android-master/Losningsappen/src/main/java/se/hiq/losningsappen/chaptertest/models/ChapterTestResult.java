package se.hiq.losningsappen.chaptertest.models;

import java.io.Serializable;

import se.hiq.losningsappen.history.Historyable;

/**
 * Created by petterstenberg on 2014-08-26.
 * <p/>
 * This model class wraps a {@link ChapterTest} with the latest result value of the performed test.
 */

public class ChapterTestResult implements Historyable, Serializable {

    private ChapterTest test;
    private double resultRatio = 0;

    public ChapterTestResult(ChapterTest test, double resultRatio) {
        this.test = test;
        this.resultRatio = resultRatio;
    }

    public ChapterTest getTest() {
        return test;
    }

    public void setTest(ChapterTest test) {
        this.test = test;
    }

    public double getResultRatio() {
        return resultRatio;
    }

    public void setResultRatio(double resultRatio) {
        this.resultRatio = resultRatio;
    }

    @Override
    public String getName() {
        return test.getTitle();
    }

    @Override
    public boolean equals(Object o) {
        return test.getTitle().equals(o);
    }
}
