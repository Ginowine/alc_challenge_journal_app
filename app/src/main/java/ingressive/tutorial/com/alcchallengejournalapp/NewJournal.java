package ingressive.tutorial.com.alcchallengejournalapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.database.Cursor;
import android.widget.Toast;

import ingressive.tutorial.com.alcchallengejournalapp.data.JournalContract;
import ingressive.tutorial.com.alcchallengejournalapp.data.JournalDbHelper;

public class NewJournal extends AppCompatActivity {

    private EditText mNewJournalTitleEditText;
    private EditText mNewJournaEditText;

    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_journal);

        JournalDbHelper dbHelper = new JournalDbHelper(this);
        mDb = dbHelper.getWritableDatabase();

        mNewJournalTitleEditText = (EditText) this.findViewById(R.id.jounal_title_edit_text);
        mNewJournaEditText = (EditText) this.findViewById(R.id.journal_edit_text);
    }


    public void addToJournal(View view){

        if (mNewJournalTitleEditText.getText().length() == 0 ||
                mNewJournaEditText.getText().length() == 0){
            return;
        }

        addNewJournal(mNewJournalTitleEditText.getText().toString(), mNewJournaEditText.getText().toString());

          //JournalActivity journalActivity = new JournalActivity();

//        journalAdapter.swapCursor(journalActivity.getAllJournal());
          //journalActivity.mAdapter.swapCursor(journalActivity.getAllJournal());

        Cursor cursor = getAllJournal();
        JournalAdapter journalAdapter = new JournalAdapter(this, cursor);
        journalAdapter.swapCursor(cursor);

        mNewJournaEditText.clearFocus();
        mNewJournalTitleEditText.getText().clear();
        mNewJournaEditText.getText().clear();

        Intent intent = new Intent(NewJournal.this, JournalActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    public Cursor getAllJournal() {
        // COMPLETED (6) Inside, call query on mDb passing in the table name and projection String [] order by COLUMN_TIMESTAMP
        return mDb.query(
                JournalContract.JournalEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                JournalContract.JournalEntry.COLUMN_TIMESTAMP
        );
    }

    private void addNewJournal(String journal, String title){
        ContentValues cv = new ContentValues();

        cv.put(JournalContract.JournalEntry.COLUMN_TITLE, title);
        cv.put(JournalContract.JournalEntry.COLUMN_NOTE, journal);
        // COMPLETED (8) call insert to run an insert query on TABLE_NAME with the ContentValues created
        //return mDb.insert(JournalContract.JournalEntry.TABLE_NAME, null, cv);

        long rowInserted = mDb.insert(JournalContract.JournalEntry.TABLE_NAME, null, cv);
        if (rowInserted != -1){
            Toast.makeText(this, "New row added, row id: " + rowInserted, Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Something wrong", Toast.LENGTH_LONG).show();
        }

    }
}
