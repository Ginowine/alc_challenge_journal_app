package ingressive.tutorial.com.alcchallengejournalapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.database.Cursor;

import ingressive.tutorial.com.alcchallengejournalapp.data.JournalContract;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.JournalViewHolder>{

    private Context mContext;
    private Cursor mCursor;

    public JournalAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
    }

    @NonNull
    @Override
    public JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.journal_list_item, parent, false);
        return new JournalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)){
            String title = mCursor.getString(mCursor.getColumnIndex(JournalContract.JournalEntry.COLUMN_TITLE));
            String notes = mCursor.getString(mCursor.getColumnIndex(JournalContract.JournalEntry.COLUMN_NOTE));

            holder.noteTitle.setText(title);
            holder.noteTextView.setText(notes);
        }

    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    class JournalViewHolder extends RecyclerView.ViewHolder {

        TextView noteTextView;

        TextView noteTitle;

        public JournalViewHolder(View itemView) {
            super(itemView);
            noteTitle = (TextView) itemView.findViewById(R.id.title_text_view);
            noteTextView = (TextView) itemView.findViewById(R.id.note_text_view);
        }

    }
}
