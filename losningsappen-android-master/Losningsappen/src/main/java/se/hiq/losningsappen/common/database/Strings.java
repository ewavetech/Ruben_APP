package se.hiq.losningsappen.common.database;

import android.net.Uri;

/**
 * Created by Naknut on 07/08/14.
 */
public class Strings {

    public static final String DB_NAME = "book.db";
    public static final String ID = "_id";
    public static final String TABLE_MARKED_SUBTASKS = "marked_subtasks";
    public static final String SUBTASK_OR_CHAPTERTEST = "subtask_or_chaptertest";
    public static final String MARKED_SUBTASKS_NAME = "name";
    public static final String CHAPTERTEST_PERSENTAGE = "chaptertest_persentage";
    public static final String MARKED_SUBTASKS_DONE = "done";
    public static final String MARKED_SUBTASKS_CHAPTER = "chapter";
    public static final String MARKED_SUBTASKS_SUBCHAPTER = "subchapter";
    public static final String MARKED_SUBTASKS_TASK = "task";
    public static final String MARKED_SUBTASKS_SUBTASK = "subtask";
    public static final String MARKED_SUBTASKS_BASE_PATH = "books";
    static final String AUTHORITY = "se.hiq.losningsappen.database";
    public static final Uri MARKED_SUBTASK_CONTENT_URI = Uri.parse("content://" + Strings.AUTHORITY + "/" + MARKED_SUBTASKS_BASE_PATH);
}
