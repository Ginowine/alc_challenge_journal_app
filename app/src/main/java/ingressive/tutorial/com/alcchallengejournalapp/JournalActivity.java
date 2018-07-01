package ingressive.tutorial.com.alcchallengejournalapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import ingressive.tutorial.com.alcchallengejournalapp.data.JournalContract;
import ingressive.tutorial.com.alcchallengejournalapp.data.JournalDbHelper;

public class JournalActivity extends AppCompatActivity {
    public JournalAdapter mAdapter;
    private SQLiteDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView journallistRecyclerView;

        // Set local attributes to corresponding views
        journallistRecyclerView = (RecyclerView) this.findViewById(R.id.all_journal_list_view);
        journallistRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        JournalDbHelper dbHelper = new JournalDbHelper(this);
        mDb = dbHelper.getWritableDatabase();

        Cursor cursor = getAllJournal();

        mAdapter = new JournalAdapter(this, cursor);

        journallistRecyclerView.setAdapter(mAdapter);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

}
