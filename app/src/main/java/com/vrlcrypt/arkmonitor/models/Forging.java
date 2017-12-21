package com.vrlcrypt.arkmonitor.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Logger;

public class Forging {
    private Long fees;
    private Long rewards;
    private Long forged;

    private static final String TAG = Forging.class.getSimpleName();

    public Long getFees() {
        return fees;
    }

    public void setFees(Long fees) {
        this.fees = fees;
    }

    public Long getRewards() {
        return rewards;
    }

    public void setRewards(Long rewards) {
        this.rewards = rewards;
    }

    public Long getForged() {
        return forged;
    }

    public void setForged(Long forged) {
        this.forged = forged;
    }

    public static Forging fromJson(JSONObject jsonObject) {
        Forging forging = new Forging();

        if (jsonObject == null) {
            return forging;
        }

        try {
            forging.fees = jsonObject.getLong("fees");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("forging.fees (%s)", e.getLocalizedMessage()));
        }

        try {
            forging.rewards = jsonObject.getLong("rewards");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("forging.rewards (%s)", e.getLocalizedMessage()));
        }

        try {
            forging.forged = jsonObject.getLong("forged");
        } catch (JSONException e) {
            Logger.getLogger(TAG).warning(String.format("forging.forged (%s)", e.getLocalizedMessage()));
        }

        return forging;
    }
}