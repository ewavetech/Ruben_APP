package se.hiq.losningsappen.common.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Naknut on 07/08/14.
 */
class BookDatabase extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;

    private static final String CREATE_MARKED_SUBTASK_TABLE = "create table if not exists " + Strings.TABLE_MARKED_SUBTASKS +
            " ( " + Strings.ID + " integer primary key autoincrement, " +
            Strings.SUBTASK_OR_CHAPTERTEST + " integer not null, " +
            Strings.CHAPTERTEST_PERSENTAGE + " real, " +
            Strings.MARKED_SUBTASKS_NAME + " text not null, " +
            Strings.MARKED_SUBTASKS_DONE + " integer, " +
            Strings.MARKED_SUBTASKS_CHAPTER + " integer, " +
            Strings.MARKED_SUBTASKS_SUBCHAPTER + " integer, " +
            Strings.MARKED_SUBTASKS_TASK + " integer, " +
            Strings.MARKED_SUBTASKS_SUBTASK + " integer ); ";

    public BookDatabase(Context context) {
        super(context, Strings.DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_MARKED_SUBTASK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(BookDatabase.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data"
        );
        db.execSQL("DROP TABLE IF EXISTS " + Strings.TABLE_MARKED_SUBTASKS);
        onCreate(db);
    }
}
