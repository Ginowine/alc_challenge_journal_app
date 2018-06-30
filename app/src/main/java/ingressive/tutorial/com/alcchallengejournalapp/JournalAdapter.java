package ingressive.tutorial.com.alcchallengejournalapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.JournalViewHolder>{

    private Context mContext;
    private int mCount;

    public JournalAdapter(Context context, int count) {
        this.mContext = context;
        // COMPLETED (10) Set the local mCount to be equal to count
        mCount = count;
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

    }

    @Override
    public int getItemCount() {
        return mCount;
    }

    class JournalViewHolder extends RecyclerView.ViewHolder {

        // Will display the guest name
        TextView nameTextView;
        // Will display the party size number
        TextView partySizeTextView;

        public JournalViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.title_text_view);
            partySizeTextView = (TextView) itemView.findViewById(R.id.note_text_view);
        }

    }
}
