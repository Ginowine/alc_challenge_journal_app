package ingressive.tutorial.com.alcchallengejournalapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class JournalDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "journal.db";

    private static final int DATABASE_VERSION = 1;

    public JournalDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE " + JournalContract.JournalEntry.TABLE_NAME + " (" +
                JournalContract.JournalEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                JournalContract.JournalEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                JournalContract.JournalEntry.COLUMN_ENTRY + " INTEGER NOT NULL, " +
                JournalContract.JournalEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_WAITLIST_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + JournalContract.JournalEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
