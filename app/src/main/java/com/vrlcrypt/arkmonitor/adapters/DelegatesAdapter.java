package com.vrlcrypt.arkmonitor.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vrlcrypt.arkmonitor.R;
import com.vrlcrypt.arkmonitor.models.Delegate;

import java.util.ArrayList;
import java.util.List;

public class DelegatesAdapter extends
        RecyclerView.Adapter<DelegatesAdapter.ViewHolder> {

    private final Context mContext;
    private List<Delegate> mDelegates;

    public DelegatesAdapter(Context context, List<Delegate> delegates) {
        mContext = context;
        mDelegates = delegates;
    }

    public void setDelegates(List<Delegate> mDelegates) {
        this.mDelegates = new ArrayList<>(mDelegates);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View delegateView = inflater.inflate(R.layout.delegate_row, parent, false);

        return new ViewHolder(delegateView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView rankTextView = holder.rankTextView;
        TextView nameTextView = holder.nameTextView;
        TextView approvalTextView = holder.approvalTextView;
        TextView productivityTextView = holder.productivityTextView;

        Delegate delegate = mDelegates.get(position);
        rankTextView.setText(String.valueOf(delegate.getRate()));
        nameTextView.setText(delegate.getUsername());

        String approval = delegate.getApproval() + mContext.getString(R.string.percent_symbol);
        approvalTextView.setText(approval);

        productivityTextView.setText(String.valueOf(delegate.getProductivity() +
                mContext.getString(R.string.percent_symbol)));
    }

    @Override
    public int getItemCount() {
        return mDelegates.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView rankTextView;
        final TextView nameTextView;
        final TextView approvalTextView;
        final TextView productivityTextView;

        ViewHolder(View itemView) {
            super(itemView);

            rankTextView = (TextView) itemView.findViewById(R.id.delegate_rank);
            nameTextView = (TextView) itemView.findViewById(R.id.delegate_name);
            approvalTextView = (TextView) itemView.findViewById(R.id.delegate_approval);
            productivityTextView = (TextView) itemView.findViewById(R.id.delegate_productivity);
        }
    }
}
