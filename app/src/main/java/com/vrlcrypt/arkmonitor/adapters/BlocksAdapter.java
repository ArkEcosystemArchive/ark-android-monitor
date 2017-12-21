package com.vrlcrypt.arkmonitor.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vrlcrypt.arkmonitor.R;
import com.vrlcrypt.arkmonitor.models.Block;
import com.vrlcrypt.arkmonitor.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vrlcypt  on 12/4/16.
 */

public class BlocksAdapter extends
        RecyclerView.Adapter<BlocksAdapter.ViewHolder> {

    private final Context mContext;
    private List<Block> mBlocks;

    public BlocksAdapter(Context context, List<Block> blocks) {
        mContext = context;
        mBlocks = blocks;
    }

    public void setBlocks(List<Block> blocks) {
        this.mBlocks = new ArrayList<>(blocks);
    }

    @Override
    public BlocksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View blockView = inflater.inflate(R.layout.block_row, parent, false);

        return new BlocksAdapter.ViewHolder(blockView);
    }

    @Override
    public void onBindViewHolder(BlocksAdapter.ViewHolder holder, int position) {
        TextView heightTextView = holder.heightTextView;
        TextView timeTextView = holder.timeTextView;
        TextView feeTextView = holder.feeTextView;
        TextView rewardTextView = holder.rewardTextView;

        Block block = mBlocks.get(position);

        CharSequence timeAgo = Utils.getTimeAgo(block.getTimestamp());

        heightTextView.setText(String.valueOf(block.getHeight()));
        timeTextView.setText(timeAgo.toString());
        feeTextView.setText(String.valueOf(Utils.convertToArkBase(block.getTotalFee())));
        rewardTextView.setText(String.valueOf(Utils.convertToArkBase(block.getReward())));
    }

    @Override
    public int getItemCount() {
        return mBlocks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView heightTextView;
        public final TextView timeTextView;
        public final TextView feeTextView;
        public final TextView rewardTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            heightTextView = (TextView) itemView.findViewById(R.id.block_height);
            timeTextView = (TextView) itemView.findViewById(R.id.block_time);
            feeTextView = (TextView) itemView.findViewById(R.id.block_fee);
            rewardTextView = (TextView) itemView.findViewById(R.id.block_reward);
        }
    }
}
