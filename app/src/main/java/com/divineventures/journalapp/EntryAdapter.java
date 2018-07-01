package com.divineventures.journalapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.ViewHolder> {

    private Context context;
    private EntryData entryData;

    public EntryAdapter(Context context, EntryData entryData) {
        this.context = context;
        this.entryData = entryData;
    }

    @Override
    public EntryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entry_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EntryAdapter.ViewHolder holder, int position) {
        holder.date.setText(entryData.getDate());
        holder.time.setText(entryData.getTime());
        holder.hint.setText(entryData.getDescription());

    }

    @Override
    public int getItemCount() {
        return getItemCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView date,hint,time;

        public ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date_tv);
            time = itemView.findViewById(R.id.time_tv);
            hint = itemView.findViewById(R.id.entry_tv);
        }
    }
}
