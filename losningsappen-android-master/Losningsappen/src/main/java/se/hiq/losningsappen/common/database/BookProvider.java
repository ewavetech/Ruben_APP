//package se.hiq.losningsappen.common.database;
//
//import android.app.SearchManager;
//import android.content.ContentProvider;
//import android.content.ContentUris;
//import android.content.ContentValues;
//import android.content.UriMatcher;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteQueryBuilder;
//import android.net.Uri;
//import android.text.TextUtils;
//
//import java.util.Arrays;
//import java.util.HashSet;
//
///**
// * Created by Naknut on 07/08/14.
// */
//public class BookProvider extends ContentProvider {
//
//    public static final String[] AVALIBLE_PROJECTIONS = {
//            Strings.ID,
//            Strings.MARKED_SUBTASKS_NAME,
//            Strings.SUBTASK_OR_CHAPTERTEST,
//            Strings.CHAPTERTEST_PERSENTAGE,
//            Strings.MARKED_SUBTASKS_DONE,
//            Strings.MARKED_SUBTASKS_CHAPTER,
//            Strings.MARKED_SUBTASKS_SUBCHAPTER,
//            Strings.MARKED_SUBTASKS_TASK,
//            Strings.MARKED_SUBTASKS_SUBTASK,
//            Strings.MARKED_SUBTASKS_BASE_PATH,
//            SearchManager.SUGGEST_COLUMN_TEXT_1,
//            SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA
//    };
//    private static final int MARKED_SUBTASKS = 10;
//    private static final int MARKED_SUBTASK_ID = 20;
//
//    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
//
//    static {
//        sURIMatcher.addURI(Strings.AUTHORITY, Strings.MARKED_SUBTASKS_BASE_PATH, MARKED_SUBTASKS);
//        sURIMatcher.addURI(Strings.AUTHORITY, Strings.MARKED_SUBTASKS_BASE_PATH + "/#", MARKED_SUBTASK_ID);
//    }
//
//    BookDatabase database;
//
//    @Override
//    public boolean onCreate() {
//        database = new BookDatabase(getContext());
//        return true;
//    }
//
//    @Override
//    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
//        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
//        checkColumns(projection);
//
//        int uriType = sURIMatcher.match(uri);
//        switch (uriType) {
//            case MARKED_SUBTASK_ID:
//                queryBuilder.appendWhere(Strings.ID + "=" + uri.getLastPathSegment());
//            case MARKED_SUBTASKS:
//                queryBuilder.setTables(Strings.TABLE_MARKED_SUBTASKS);
//                break;
//            default:
//                throw new IllegalArgumentException("Unknown URI: " + uri);
//        }
//
//        SQLiteDatabase db = database.getWritableDatabase();
//        Cursor cursor = queryBuilder.query(db, projection, selection,
//                selectionArgs, null, null, sortOrder);
//        // make sure that potential listeners are getting notified
//        cursor.setNotificationUri(getContext().getContentResolver(), uri);
//
//        return cursor;
//    }
//
//    @Override
//    public String getType(Uri uri) {
//        return null;
//    }
//
//    @Override
//    public Uri insert(Uri uri, ContentValues values) {
//        int uriType = sURIMatcher.match(uri);
//        SQLiteDatabase sqlDB = database.getWritableDatabase();
//        long id;
//        switch (uriType) {
//            case MARKED_SUBTASKS:
//                id = sqlDB.insert(Strings.TABLE_MARKED_SUBTASKS, null, values);
//                break;
//            default:
//                throw new IllegalArgumentException("Unknown URI: " + uri);
//        }
//        Uri testuri = ContentUris.withAppendedId(Strings.MARKED_SUBTASK_CONTENT_URI, id);
//        getContext().getContentResolver().notifyChange(uri, null);
//        return testuri;
//    }
//
//    @Override
//    public final int bulkInsert(Uri url, ContentValues[] values) {
//        String table;
//        int uriType = sURIMatcher.match(url);
//        switch (uriType) {
//            case MARKED_SUBTASKS:
//                table = Strings.TABLE_MARKED_SUBTASKS;
//                break;
//            default:
//                throw new IllegalArgumentException("Unknown URI: " + url);
//        }
//        SQLiteDatabase sqlDB = database.getWritableDatabase();
//        sqlDB.beginTransaction();
//        for (ContentValues value : values) {
//            sqlDB.insert(table, null, value);
//        }
//        sqlDB.endTransaction();
//        return values.length;
//    }
//
//    @Override
//    public int delete(Uri uri, String selection, String[] selectionArgs) {
//        int uriType = sURIMatcher.match(uri);
//        SQLiteDatabase sqlDB = database.getWritableDatabase();
//        int rowsDeleted;
//        switch (uriType) {
//            case MARKED_SUBTASKS:
//                rowsDeleted = sqlDB.delete(Strings.TABLE_MARKED_SUBTASKS, selection,
//                        selectionArgs);
//                break;
//            case MARKED_SUBTASK_ID:
//                String id = uri.getLastPathSegment();
//                if (TextUtils.isEmpty(selection)) {
//                    rowsDeleted = sqlDB.delete(Strings.TABLE_MARKED_SUBTASKS,
//                            Strings.ID + "=" + id,
//                            null);
//                } else {
//                    rowsDeleted = sqlDB.delete(Strings.TABLE_MARKED_SUBTASKS,
//                            Strings.ID + "=" + id
//                                    + " and " + selection,
//                            selectionArgs);
//                }
//                break;
//            default:
//                throw new IllegalArgumentException("Unknown URI: " + uri);
//        }
//        getContext().getContentResolver().notifyChange(uri, null);
//        return rowsDeleted;
//    }
//
//    @Override
//    public int update(Uri uri, ContentValues values, String selection,
//                      String[] selectionArgs) {
//
//        int uriType = sURIMatcher.match(uri);
//        SQLiteDatabase sqlDB = database.getWritableDatabase();
//        int rowsUpdated;
//        switch (uriType) {
//            case MARKED_SUBTASKS:
//                rowsUpdated = sqlDB.update(Strings.TABLE_MARKED_SUBTASKS,
//                        values,
//                        selection,
//                        selectionArgs);
//                break;
//            case MARKED_SUBTASK_ID:
//                String taskId = uri.getLastPathSegment();
//                if (TextUtils.isEmpty(selection)) {
//                    rowsUpdated = sqlDB.update(Strings.TABLE_MARKED_SUBTASKS,
//                            values,
//                            Strings.ID + "=" + taskId,
//                            null);
//                } else {
//                    rowsUpdated = sqlDB.update(Strings.TABLE_MARKED_SUBTASKS,
//                            values,
//                            Strings.ID + "=" + taskId
//                                    + " and "
//                                    + selection,
//                            selectionArgs);
//                }
//                break;
//            default:
//                throw new IllegalArgumentException("Unknown URI: " + uri);
//        }
//        getContext().getContentResolver().notifyChange(uri, null);
//        return rowsUpdated;
//    }
//
//    private void checkColumns(String[] projection) {
//        if (projection != null) {
//            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
//            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(AVALIBLE_PROJECTIONS));
//            // check if all columns which are requested are available
//            if (!availableColumns.containsAll(requestedColumns)) {
//                throw new IllegalArgumentException("Unknown columns in projection");
//            }
//        }
//    }
//}
