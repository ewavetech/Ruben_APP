package se.hiq.losningsappen.chaptertest.models;

import java.util.List;

/**
 * Created by Naknut on 01/08/14.
 */
public class ChapterTestContext {

    private static ChapterTestContext instance = null;
    private List<ChapterTest> chapterTests;

    private ChapterTestContext() {
    }

    public static ChapterTestContext getInstance() {
        if (instance == null)
            instance = new ChapterTestContext();
        return instance;
    }

    public List<ChapterTest> getChapterTests() {
        return chapterTests;
    }

    public void setChapterTests(List<ChapterTest> chapterTests) {
        this.chapterTests = chapterTests;
    }
}
