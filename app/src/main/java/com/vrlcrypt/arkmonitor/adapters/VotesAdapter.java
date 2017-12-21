package com.vrlcrypt.arkmonitor.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vrlcrypt.arkmonitor.R;
import com.vrlcrypt.arkmonitor.models.Delegate;
import com.vrlcrypt.arkmonitor.models.Votes;

import java.util.List;

public class VotesAdapter extends
        RecyclerView.Adapter<VotesAdapter.ViewHolder> {

    private final List<Delegate> mDelegates;

    public VotesAdapter(Votes votes) {
        mDelegates = votes.getDelegates();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View voteView = inflater.inflate(R.layout.vote_row, parent, false);

        return new ViewHolder(voteView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView rankTextView = holder.rankTextView;
        TextView nameTextView = holder.nameTextView;
        TextView addressTextView = holder.addressTextView;

        Delegate delegate = mDelegates.get(position);
        rankTextView.setText(String.valueOf(delegate.getRate()));
        nameTextView.setText(delegate.getUsername());
        addressTextView.setText(delegate.getAddress());
    }

    @Override
    public int getItemCount() {
        return mDelegates.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView rankTextView;
        final TextView nameTextView;
        final TextView addressTextView;

        ViewHolder(View itemView) {

            super(itemView);

            rankTextView = (TextView) itemView.findViewById(R.id.vote_delegate_rank);
            nameTextView = (TextView) itemView.findViewById(R.id.vote_delegate_name);
            addressTextView = (TextView) itemView.findViewById(R.id.vote_delegate_address);
        }
    }
}