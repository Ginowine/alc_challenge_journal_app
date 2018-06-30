package ingressive.tutorial.com.alcchallengejournalapp.data;

import android.provider.BaseColumns;

public class JournalContract {

    public static final class JournalEntry implements BaseColumns {

        public static final String TABLE_NAME = "journal";
        public static final String COLUMN_TITLE = "journalTitle";
        public static final String COLUMN_ENTRY = "note";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
