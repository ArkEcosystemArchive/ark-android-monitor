package com.vrlcrypt.arkmonitor.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Delegate implements Comparable<Delegate> {
    private String username;
    private String address;
    private String publicKey;
    private Long vote;
    private Long producedblocks;
    private Long missedblocks;
    private Long rate;
    private Double productivity;
    private Double approval;

    private static final String TAG = Delegate.class.getSimpleName();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public Long getVote() {
        return vote;
    }

    public void setVote(Long vote) {
        this.vote = vote;
    }

    public Long getProducedblocks() {
        return producedblocks;
    }

    public void setProducedblocks(Long producedblocks) {
        this.producedblocks = producedblocks;
    }

    public Long getMissedblocks() {
        return missedblocks;
    }

    public void setMissedblocks(Long missedblocks) {
        this.missedblocks = missedblocks;
    }

    public Long getRate() {
        return rate;
    }

    public void setRate(Long rate) {
        this.rate = rate;
    }

    public Double getProductivity() {
        return productivity;
    }

    public void setProductivity(Double productivity) {
        this.productivity = productivity;
    }

    public Double getApproval() {
        return approval;
    }

    public void setApproval(Double approval) {
        this.approval = approval;
    }

    public static Delegate fromJson(JSONObject jsonObject){
        Delegate delegate = new Delegate();

        if (jsonObject == null) {
            return delegate;
        }

        try {
            delegate.username = jsonObject.getString("username");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("delegate.username (%s)", e.getLocalizedMessage()));
        }

        try {
            delegate.address = jsonObject.getString("address");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("delegate.address (%s)", e.getLocalizedMessage()));
        }

        try {
            delegate.publicKey = jsonObject.getString("publicKey");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("delegate.publicKey (%s)", e.getLocalizedMessage()));
        }

        try {
            delegate.vote = jsonObject.getLong("vote");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("delegate.vote (%s)", e.getLocalizedMessage()));
        }

        try {
            delegate.producedblocks = jsonObject.getLong("producedblocks");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("delegate.producedblocks (%s)", e.getLocalizedMessage()));
        }

        try {
            delegate.missedblocks = jsonObject.getLong("missedblocks");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("delegate.missedblocks (%s)", e.getLocalizedMessage()));
        }

        try {
            delegate.rate = jsonObject.getLong("rate");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("delegate.rate (%s)", e.getLocalizedMessage()));
        }

        try {
            delegate.productivity = jsonObject.getDouble("productivity");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("delegate.productivity (%s)", e.getLocalizedMessage()));
        }

        try {
            delegate.approval = jsonObject.getDouble("approval");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("delegate.approval (%s)", e.getLocalizedMessage()));
        }

        return delegate;
    }

    public static List<Delegate> fromJson(JSONArray delegatesJsonArray) {
        List<Delegate> delegates = new ArrayList<>();

        if (delegatesJsonArray != null) {
            for (int i = 0; i < delegatesJsonArray.length(); i++) {
                JSONObject delegateJsonObject = null;
                try {
                    delegateJsonObject = delegatesJsonArray.getJSONObject(i);
                } catch (JSONException e) {
                    Logger.getLogger(TAG).warning(e.getLocalizedMessage());
                }

                if (delegateJsonObject != null) {
                    delegates.add(Delegate.fromJson(delegateJsonObject));
                }
            }
        }

        return delegates;
    }

    @Override
    public int compareTo(Delegate delegate) {
        return rate.compareTo(delegate.rate);
    }

}