package com.vrlcrypt.arkmonitor.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vrlcrypt.arkmonitor.R;
import com.vrlcrypt.arkmonitor.models.Peer;

import java.util.List;

public class PeersAdapter extends
        RecyclerView.Adapter<PeersAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Peer> mPeers;

    public PeersAdapter(Context context, List<Peer> peers) {
        mContext = context;
        mPeers = peers;
    }

    @Override
    public PeersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.peer_row, parent, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(PeersAdapter.ViewHolder viewHolder, int position) {

        Peer peer = mPeers.get(position);

        TextView ipAddressTextView = viewHolder.ipAddressTextView;
        TextView portTextView = viewHolder.portTextView;
        ImageView statusTextView = viewHolder.statusImageView;
        TextView versionTextView = viewHolder.versionTextView;

        ipAddressTextView.setText(peer.getIp());
        portTextView.setText(String.valueOf(peer.getPort()));

        Peer.PeerStatus peerState = Peer.PeerStatus.fromStatus(peer.getStatus());

        switch (peerState){
            case EUNAVAILABLE:
                statusTextView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_banned));
                break;
            case ETIMEOUT:
                statusTextView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_disconnected));
                break;
            case OK:
                statusTextView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_connected));
                break;
            default:
                statusTextView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_disconnected));
                break;
        }

        versionTextView.setText(peer.getVersion());
    }

    @Override
    public int getItemCount() {
        return mPeers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView ipAddressTextView;
        final TextView portTextView;
        final ImageView statusImageView;
        final TextView versionTextView;

        ViewHolder(View itemView) {
            super(itemView);

            ipAddressTextView = (TextView) itemView.findViewById(R.id.peer_ip_address);
            portTextView = (TextView) itemView.findViewById(R.id.peer_port);
            statusImageView = (ImageView) itemView.findViewById(R.id.peer_status);
            versionTextView = (TextView) itemView.findViewById(R.id.peer_version);
        }
    }
}
